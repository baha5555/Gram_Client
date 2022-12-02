package com.example.gramclient.presentation.mainScreen

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gramclient.PreferencesName
import com.example.gramclient.Resource
import com.example.gramclient.data.AppRepositoryImpl
import com.example.gramclient.domain.mainScreen.*
import com.example.gramclient.domain.mainScreen.order.*
import com.example.gramclient.presentation.mainScreen.states.*
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainViewModel:ViewModel() {
    private val repository= AppRepositoryImpl
    private val getTariffsUseCase= GetTariffsUseCase(repository)
    private val getAllowancesUseCase= GetAllowancesUseCase(repository)
    private val getAddressByPointUseCase= GetAddressByPointUseCase(repository)
    private val searchAddressUseCase= SearchAddressUseCase(repository)
    private val createOrderUseCase= CreateOrderUseCase(repository)
    private val getPriceUseCase= GetPriceUseCase(repository)



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


    private val _sendOrder = mutableStateOf(OrderModel(tariff_id = 1))
    val sendOrder: MutableLiveData<OrderModel> = MutableLiveData<OrderModel>()

    val from_address = MutableLiveData<Address>(Address("Откуда?", 0, "", ""))

    private val _to_address = mutableListOf<Address>(Address("Куда?", 0, "", ""))
    val to_address = MutableLiveData<MutableList<Address>>(_to_address)

    val selectedTariff :MutableLiveData<TariffsResult>? = MutableLiveData<TariffsResult>(TariffsResult(1, "Эконом", 12))

    private var _selectedAllowances: MutableList<AllowanceRequest> = mutableListOf<AllowanceRequest>()
    val selectedAllowances = MutableLiveData<MutableList<AllowanceRequest>>(_selectedAllowances)


    fun updateFromAddress(value:Address) {
        Log.e("TariffsResponse", "from_address->\n ${from_address.value}")
        from_address.value=value
        Log.e("TariffsResponse", "from_address->\n ${from_address.value}")
    }

    fun updateToAddress(index: Int, value:Address?) {
        if(value != null && index <= _to_address.size){
            _to_address[index]=value
            to_address.value=_to_address
        }
    }

    fun updateSelectedTariff(
        value: TariffsResult,
        preferences: SharedPreferences
    ) {
        selectedTariff?.value=value
        _selectedAllowances = mutableListOf()
        selectedAllowances.value=_selectedAllowances
        getPrice(preferences)
    }

    fun includeAllowance(toDesiredAllowance: ToDesiredAllowance, preferences: SharedPreferences){
        if(toDesiredAllowance.isSelected.value){
            Log.e("TariffsResponse", "selectedAllowances->\n ${_selectedAllowances}\n ${selectedAllowances.value}")
            _selectedAllowances.add(toDesiredAllowance.toAllowanceRequest())
            selectedAllowances.value=_selectedAllowances
            getPrice(preferences)
            Log.e("TariffsResponse", "selectedAllowances->\n ${_selectedAllowances}\n ${selectedAllowances.value}")
        }else{
            Log.e("TariffsResponse", "selectedAllowances->\n ${_selectedAllowances}\n ${selectedAllowances.value}")
            _selectedAllowances.remove(toDesiredAllowance.toAllowanceRequest())
            selectedAllowances.value=_selectedAllowances
            getPrice(preferences)
            Log.e("TariffsResponse", "selectedAllowances->\n ${_selectedAllowances}\n ${selectedAllowances.value}")
        }
    }

    fun getTariffs(token:String){
        getTariffsUseCase.invoke(token="Bearer $token").onEach { result: Resource<TariffsResponse> ->
            when (result){
                is Resource.Success -> {
                    try {
                        val tariffsResponse: TariffsResponse? = result.data
                        _stateTariffs.value =
                            TariffsResponseState(response = tariffsResponse?.result)
                        Log.e("TariffsResponse", "TariffsResponse->\n ${_stateTariffs.value}")
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

    fun getAllowancesByTariffId(token:String, tariff_id:Int){
        getAllowancesUseCase.invoke(token="Bearer $token", tariff_id = tariff_id).onEach { result: Resource<AllowancesResponse> ->
            when (result){
                is Resource.Success -> {
                    try {
                        val allowancesResponse: AllowancesResponse? = result.data
                        _stateAllowances.value =
                            AllowancesResponseState(response = allowancesResponse?.result?.map { it.toDesiredAllowance() })
                        Log.e("TariffsResponse", "AllowancesResponseError->\n ${_stateAllowances.value}")
                    }catch (e: Exception) {
                        Log.d("Exception", "${e.message} Exception")
                    }
                }
                is Resource.Error -> {
                    Log.e("AllowancesResponse", "AllowancesResponse->\n ${result.message}")
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

    fun getAddressByPoint(token: String, lng: Double, lat: Double){
        getAddressByPointUseCase.invoke(token="Bearer $token", lng, lat).onEach { result: Resource<AddressByPointResponse> ->
            when (result){
                is Resource.Success -> {
                    try {
                        val addressResponse: AddressByPointResponse? = result.data
                        _stateAddressPoint.value =
                            AddressByPointResponseState(response = addressResponse?.result)
                        Log.e("TariffsResponse", "AllowancesResponseError->\n ${_stateAddressPoint.value}")
                        updateFromAddress(
                            Address(_stateAddressPoint.value.response!!.name,
                            _stateAddressPoint.value.response!!.id,
                            _stateAddressPoint.value.response!!.lat,
                            _stateAddressPoint.value.response!!.lng)
                        )
                    }catch (e: Exception) {
                        Log.d("Exception", "${e.message} Exception")
                    }
                }
                is Resource.Error -> {
                    Log.e("AllowancesResponse", "AllowancesResponse->\n ${result.message}")
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
    fun getActualLocation(context: Context, token: String) {
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
                getAddressByPoint(token, it.longitude, it.latitude)
                Log.e("TariffsResponse","Location - > ${it.longitude}  + ${it.latitude}")
            }
            else{
                Log.e("NULL", "NULL")
            }
        }

    }
    fun searchAddress(token: String, addressName: String){
        searchAddressUseCase.invoke(token="Bearer $token", addressName).onEach { result: Resource<SearchAddressResponse> ->
            when (result){
                is Resource.Success -> {
                    try {
                        val addressResponse: SearchAddressResponse? = result.data
                        _stateSearchAddress.value =
                            SearchAddressResponseState(response = addressResponse?.result)
                        Log.e("TariffsResponse", "SearchAddressResponse->\n ${_stateSearchAddress.value}")
                    }catch (e: Exception) {
                        Log.d("Exception", "${e.message} Exception")
                    }
                }
                is Resource.Error -> {
                    Log.e("TariffsResponse", "TariffsResponseError->\n ${result.message}")
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

    fun createOrder(preferences: SharedPreferences) {
        createOrderUseCase.invoke(
            token="Bearer ${preferences.getString(PreferencesName.ACCESS_TOKEN, "").toString()}",
            dop_phone = null,
            from_address = if(from_address.value?.id != 0) from_address.value?.id else null,
            to_addresses = if(to_address.value?.get(0)!!.id != 0) listOf(AddressModel(to_address.value?.get(0)!!.id)) else null,
            comment = null,
            tariff_id = selectedTariff?.value?.id ?: 1,
            allowances= if(selectedAllowances.value?.isNotEmpty() == true) Gson().toJson(selectedAllowances.value) else null
        ).onEach { result: Resource<OrderResponse> ->
            when (result){
                is Resource.Success -> {
                    try {
                        val response: OrderResponse? = result.data
                        _stateCreateOrder.value =
                            OrderResponseState(response = response)
                        Log.e("TariffsResponse", "OrderResponse->\n ${_stateCreateOrder.value}")
                    }catch (e: Exception) {
                        Log.d("Exception", "${e.message} Exception")
                    }
                }
                is Resource.Error -> {
                    Log.e("TariffsResponse", "OrderResponseError->\n ${result.message}")
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

    fun getPrice(preferences: SharedPreferences) {
        getPriceUseCase.invoke(
            token="Bearer ${preferences.getString(PreferencesName.ACCESS_TOKEN, "").toString()}",
            tariff_id = selectedTariff?.value?.id ?: 1,
            allowances = if(selectedAllowances.value?.isNotEmpty() == true) Gson().toJson(selectedAllowances.value) else null,
            from_address = if(from_address.value?.id != 0) from_address.value?.id else null,
            to_addresses = if(to_address.value?.get(0)!!.id != 0) listOf(AddressModel(to_address.value?.get(0)!!.id)) else null
        ).onEach { result: Resource<CalculateResponse> ->
            when (result){
                is Resource.Success -> {
                    try {
                        val response: CalculateResponse? = result.data
                        _stateCalculate.value =
                            CalculateResponseState(response = response)
                        Log.e("TariffsResponse", "CalculateResponse->\n ${_stateCalculate.value}")
                    }catch (e: Exception) {
                        Log.d("Exception", "${e.message} Exception")
                    }
                }
                is Resource.Error -> {
                    Log.e("TariffsResponse", "CalculateResponseError->\n ${result.message}")
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
}