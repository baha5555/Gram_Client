package com.example.gramclient.presentation.screens.main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gramclient.utils.Constants
import com.example.gramclient.utils.Resource
import com.example.gramclient.domain.mainScreen.*
import com.example.gramclient.domain.mainScreen.fast_address.FastAddressesResponse
import com.example.gramclient.domain.mainScreen.fast_address.FastAddressesUseCase
import com.example.gramclient.domain.mainScreen.order.*
import com.example.gramclient.presentation.screens.main.states.*
import com.example.gramclient.presentation.screens.order.OrderExecutionViewModel
import com.example.gramclient.utils.Values
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getTariffsUseCase: GetTariffsUseCase,
    private val getAllowancesUseCase: GetAllowancesUseCase,
    private val getAddressByPointUseCase: GetAddressByPointUseCase,
    private val searchAddressUseCase: SearchAddressUseCase,
    private val createOrderUseCase: CreateOrderUseCase,
    private val getPriceUseCase: GetPriceUseCase,
    private val fastAddressesUseCase: FastAddressesUseCase
):ViewModel() {

    private val _stateTariffs = mutableStateOf(TariffsResponseState())
    val stateTariffs: State<TariffsResponseState> = _stateTariffs

    private val _stateAllowances = mutableStateOf(AllowancesResponseState())
    val stateAllowances: State<AllowancesResponseState> = _stateAllowances

    private val _stateAddressPoint = mutableStateOf(AddressByPointResponseState())
    val stateAddressPoint: State<AddressByPointResponseState> = _stateAddressPoint

    private val _stateSearchAddress = mutableStateOf(SearchAddressResponseState())
    val stateSearchAddress: State<SearchAddressResponseState> = _stateSearchAddress

    private val _stateCalculate = mutableStateOf(CalculateResponseState())
    val stateCalculate: State<CalculateResponseState> = _stateCalculate

    private val _stateCreateOrder = mutableStateOf(OrderResponseState())
    val stateCreateOrder: State<OrderResponseState> = _stateCreateOrder

    val selectedTariff :MutableLiveData<TariffsResult>? = MutableLiveData<TariffsResult>(TariffsResult(1, "Эконом", 12))

    private var _selectedAllowances: MutableList<AllowanceRequest> = mutableListOf<AllowanceRequest>()
    val selectedAllowances = MutableLiveData<MutableList<AllowanceRequest>>(_selectedAllowances)

    private val _toAddress = mutableStateOf(listOf<Address>(Address("", 0, "", "")))
    val toAddress: State<List<Address>> = _toAddress

    private val _fromAddress = mutableStateOf(Address("", 0, "", ""))
    val fromAddress: State<Address> = _fromAddress

    @SuppressLint("NewApi")
    private val _planTrip = mutableStateOf<String>("")
    val planTrip = _planTrip

    private val _dopPhone = mutableStateOf("")
    val dopPhone = _dopPhone

    private val _commentToOrder = mutableStateOf("")
    val commentToOrder = _commentToOrder

    fun updateFromAddress(address:Address) {
        _fromAddress.value = address
        Log.i("addresses", "From-"+address)
    }

    fun updatePlanTrip(plan:String){
        _planTrip.value = plan
    }

    fun updateDopPhone(phone:String){
        _dopPhone.value = phone
    }

    fun updateCommentToOrder(comment:String){
        _commentToOrder.value = comment
    }

    fun updateToAddress(address:Address?) {
        if(address != null){
            _toAddress.value = listOf(address)
            Log.i("addresses", "To-"+_toAddress.value)
        }
    }

    fun updateSelectedTariff(
        value: TariffsResult,
    ) {
        selectedTariff?.value=value
        _selectedAllowances = mutableListOf()
        selectedAllowances.value=_selectedAllowances
        getPrice()
    }

    fun includeAllowance(toDesiredAllowance: ToDesiredAllowance){
        if(toDesiredAllowance.isSelected.value){
            Log.e("IncludeAllowance", "selectedAllowances->\n ${_selectedAllowances}\n ${selectedAllowances.value}")
            _selectedAllowances.add(toDesiredAllowance.toAllowanceRequest())
            selectedAllowances.value=_selectedAllowances
            getPrice()
            Log.e("IncludeAllowance", "selectedAllowances->\n ${_selectedAllowances}\n ${selectedAllowances.value}")
        }else{
            Log.e("IncludeAllowance", "selectedAllowances->\n ${_selectedAllowances}\n ${selectedAllowances.value}")
            _selectedAllowances.remove(toDesiredAllowance.toAllowanceRequest())
            selectedAllowances.value=_selectedAllowances
            getPrice()
            Log.e("IncludeAllowance", "selectedAllowances->\n ${_selectedAllowances}\n ${selectedAllowances.value}")
        }
    }

    fun getTariffs(){
        getTariffsUseCase.invoke().onEach { result: Resource<TariffsResponse> ->
            when (result){
                is Resource.Success -> {
                    try {
                        val tariffsResponse: TariffsResponse? = result.data
                        _stateTariffs.value =
                            TariffsResponseState(response = tariffsResponse?.result)
                        Log.e("TariffsResponse", "TariffsResponseSuccess->\n ${_stateTariffs.value}")
                    }catch (e: Exception) {
                        Log.d("Exception", "${e.message} Exception")
                    }
                }
                is Resource.Error -> {
                    Log.e("TariffsResponse", "TariffsResponseError->\n ${result.message}")
                    _stateTariffs.value = TariffsResponseState(
                        error = "${result.message}"
                    )
                }
                is Resource.Loading -> {
                    _stateTariffs.value = TariffsResponseState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getAllowancesByTariffId(tariff_id:Int){
        getAllowancesUseCase.invoke(tariff_id = tariff_id).onEach { result: Resource<AllowancesResponse> ->
            when (result){
                is Resource.Success -> {
                    try {
                        val allowancesResponse: AllowancesResponse? = result.data
                        _stateAllowances.value =
                            AllowancesResponseState(response = allowancesResponse?.result?.map { it.toDesiredAllowance() })
                        Log.e("AllowancesResponse", "AllowancesResponseSuccess->\n ${_stateAllowances.value}")
                    }catch (e: Exception) {
                        Log.d("Exception", "${e.message} Exception")
                    }
                }
                is Resource.Error -> {
                    Log.e("AllowancesResponse", "AllowancesResponseError->\n ${result.message}")
                    _stateAllowances.value = AllowancesResponseState(
                        error = "${result.message}"
                    )
                }
                is Resource.Loading -> {
                    _stateAllowances.value = AllowancesResponseState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getAddressByPoint(lng: Double, lat: Double){
        getAddressByPointUseCase.invoke(lng, lat).onEach { result: Resource<AddressByPointResponse> ->
            when (result){
                is Resource.Success -> {
                    try {
                        val addressResponse: AddressByPointResponse? = result.data
                        _stateAddressPoint.value =
                            AddressByPointResponseState(response = addressResponse?.result)
                        Log.e("AddressByPointResponse", "AddressByPointResponseSuccess->\n ${_stateAddressPoint.value}")
                        updateFromAddress(
                            Address(_stateAddressPoint.value.response!!.name,
                            _stateAddressPoint.value.response!!.id,
                            _stateAddressPoint.value.response!!.lat,
                            _stateAddressPoint.value.response!!.lng)
                        )
                    }catch (e: Exception) {
                        Log.d("AddressByPointResponse", "${e.message} Exception")
                    }
                }
                is Resource.Error -> {
                    Log.e("AddressByPointResponse", "AddressByPointResponseError->\n ${result.message}")
                    _stateAddressPoint.value = AddressByPointResponseState(
                        error = "${result.message}"
                    )
                }
                is Resource.Loading -> {
                    _stateAddressPoint.value = AddressByPointResponseState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getActualLocation(context: Context) {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
            context)
        val task = fusedLocationProviderClient.lastLocation

        if (ActivityCompat
                .checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(context as Activity, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 101)
            return
        }

        task.addOnSuccessListener {
            if (it != null){
                getAddressByPoint(it.longitude, it.latitude)
                Log.e("ActualLocation","Location - > ${it.longitude}  + ${it.latitude}")
            }
            else{
                Log.e("ActualLocation", "NULL")
            }
        }

    }

    fun searchAddress(addressName: String){
        searchAddressUseCase.invoke(addressName).onEach { result: Resource<SearchAddressResponse> ->
            when (result){
                is Resource.Success -> {
                    try {
                        val addressResponse: SearchAddressResponse? = result.data
                        _stateSearchAddress.value =
                            SearchAddressResponseState(response = addressResponse?.result)
                        Log.e("SearchAddressResponse", "SearchAddressResponseSuccess->\n ${_stateSearchAddress.value}")
                    }catch (e: Exception) {
                        Log.d("SearchAddressResponse", "${e.message} Exception")
                    }
                }
                is Resource.Error -> {
                    Log.e("SearchAddressResponse", "TariffsResponseError->\n ${result.message}")
                    _stateSearchAddress.value = SearchAddressResponseState(
                        error = "${result.message}"
                    )
                }
                is Resource.Loading -> {
                    _stateSearchAddress.value = SearchAddressResponseState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun createOrder(orderExecutionViewModel: OrderExecutionViewModel) {
        createOrderUseCase.invoke(
            dop_phone = if(_dopPhone.value!="")_dopPhone.value else null,
            from_address = if(fromAddress.value.id != 0) fromAddress.value.id else null,
            to_addresses = if(toAddress.value[0].id != 0) listOf(AddressModel(toAddress.value.get(0).id)) else null,
            comment = if(_commentToOrder.value!="")_commentToOrder.value else null,
            tariff_id = selectedTariff?.value?.id ?: 1,
            allowances= if(selectedAllowances.value?.isNotEmpty() == true) Gson().toJson(selectedAllowances.value) else null,
            date_time = if(_planTrip.value!="") _planTrip.value else null
        ).onEach { result: Resource<OrderResponse> ->
            when (result){
                is Resource.Success -> {
                    try {
                        updateDopPhone("")
                        updateCommentToOrder("")
                        val response: OrderResponse? = result.data
                        _stateCreateOrder.value =
                            OrderResponseState(response = response)
                        Log.e("OrderResponse", "OrderResponseSuccess->\n ${_stateCreateOrder.value}\n -> ${_planTrip.value}")
                        orderExecutionViewModel.getActiveOrders()
                    }catch (e: Exception) {
                        Log.d("OrderResponse", "${e.message} Exception")
                    }
                }
                is Resource.Error -> {
                    Log.e("OrderResponse", "OrderResponseError->\n ${result.message}")
                    _stateCreateOrder.value = OrderResponseState(
                        error = "${result.message}"
                    )
                }
                is Resource.Loading -> {
                    _stateCreateOrder.value = OrderResponseState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getPrice() {
        getPriceUseCase.invoke(
            tariff_id = selectedTariff?.value?.id ?: 1,
            allowances = if(selectedAllowances.value?.isNotEmpty() == true) Gson().toJson(selectedAllowances.value) else null,
            from_address = if(fromAddress.value.id != 0) fromAddress.value.id else null,
            to_addresses = if(toAddress.value[0].id != 0) listOf(AddressModel(toAddress.value[0].id)) else null
        ).onEach { result: Resource<CalculateResponse> ->
            when (result){
                is Resource.Success -> {
                    try {
                        val response: CalculateResponse? = result.data
                        _stateCalculate.value =
                            CalculateResponseState(response = response)
                        Log.e("CalculateResponse", "CalculateResponseSuccess->\n ${_stateCalculate.value}")
                    }catch (e: Exception) {
                        Log.d("CalculateResponse", "${e.message} Exception")
                    }
                }
                is Resource.Error -> {
                    Log.e("CalculateResponse", "CalculateResponseError->\n ${result.message}")
                    _stateCalculate.value = CalculateResponseState(
                        error = "${result.message}"
                    )
                }
                is Resource.Loading -> {
                    _stateCalculate.value = CalculateResponseState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getAddressFromMap(
        lng: Double,
        lat: Double,
        WHICH_ADDRESS: MutableState<String>
    ){
        getAddressByPointUseCase.invoke(lng, lat).onEach { result: Resource<AddressByPointResponse> ->
            when (result){
                is Resource.Success -> {
                    try {
                        val addressResponse: AddressByPointResponse? = result.data
                        _stateAddressPoint.value =
                            AddressByPointResponseState(response = addressResponse?.result)
                        Log.e("AddressByPointResponse", "AddressByPointResponseSuccess->\n ${_stateAddressPoint.value}")
                        when(WHICH_ADDRESS.value){
                            Constants.FROM_ADDRESS -> {
                                updateFromAddress(
                                    Address(_stateAddressPoint.value.response!!.name,
                                        _stateAddressPoint.value.response!!.id,
                                        _stateAddressPoint.value.response!!.lat,
                                        _stateAddressPoint.value.response!!.lng)
                                )
                            }
                            Constants.TO_ADDRESS -> {
                                updateToAddress(
                                    Address(_stateAddressPoint.value.response!!.name,
                                        _stateAddressPoint.value.response!!.id,
                                        _stateAddressPoint.value.response!!.lat,
                                        _stateAddressPoint.value.response!!.lng)
                                )
                            }
                        }
                        Log.e("singleTapConfirmedHelper", "$toAddress")

                    }catch (e: Exception) {
                        Log.d("AddressByPointResponse", "${e.message} Exception")
                    }
                }
                is Resource.Error -> {
                    Log.e("AddressByPointResponse", "AddressByPointResponseError->\n ${result.message}")
                    _stateAddressPoint.value = AddressByPointResponseState(
                        error = "${result.message}"
                    )
                }
                is Resource.Loading -> {
                    _stateAddressPoint.value = AddressByPointResponseState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    private val _stateFastAddress = mutableStateOf(FastAddressesResponseState())
    val stateFastAddress: State<FastAddressesResponseState> = _stateFastAddress
    fun getFastAddresses(){
        fastAddressesUseCase.invoke().onEach { result: Resource<FastAddressesResponse> ->
            when(result){
                is Resource.Success -> {
                    try {
                        val fastAddressesResponse: FastAddressesResponse? = result.data
                        _stateFastAddress.value = FastAddressesResponseState(response = fastAddressesResponse?.result)
                        Log.e("FastAddressesResponse", "FastAddressesResponseResponseSuccess->\n ${result.data}")
                    }catch (e: Exception) {
                        Log.d("FastAddressesResponse", "${e.message} Exception")
                    }
                }
                is Resource.Error -> {
                    Log.e("FastAddressesResponse", "FastAddressesResponseError->\n ${result.message}")
                    _stateFastAddress.value = FastAddressesResponseState(
                        error = "${result.message}"
                    )
                }
                is Resource.Loading -> {
                    _stateFastAddress.value = FastAddressesResponseState(isLoading = true)
                }
            }

        }.launchIn(viewModelScope)
    }
}