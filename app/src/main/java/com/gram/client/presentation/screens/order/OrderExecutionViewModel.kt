package com.gram.client.presentation.screens.order

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Vibrator
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.gram.client.domain.mainScreen.*
import com.gram.client.domain.mainScreen.order.*
import com.gram.client.domain.mainScreen.order.connectClientWithDriver.connectClientWithDriverResponse
import com.gram.client.domain.orderExecutionScreen.*
import com.gram.client.domain.orderExecutionScreen.active.ActiveOrdersResponse
import com.gram.client.domain.orderExecutionScreen.active.AllActiveOrdersResult
import com.gram.client.domain.orderExecutionScreen.reason.*
import com.gram.client.presentation.screens.main.states.AddressByPointResponseState
import com.gram.client.presentation.screens.main.states.AllowancesResponseState
import com.gram.client.presentation.screens.main.states.CalculateResponseState
import com.gram.client.presentation.screens.main.states.CancelOrderResponseState
import com.gram.client.presentation.screens.main.states.SearchAddressResponseState
import com.gram.client.presentation.screens.map.MapController
import com.gram.client.presentation.screens.map.map
import com.gram.client.presentation.screens.order.states.GetRatingReasonsResponseState
import com.gram.client.presentation.screens.order.states.GetReasonsResponseState
import com.gram.client.utils.Constants
import com.gram.client.utils.Resource
import com.gram.client.utils.Routes
import com.gram.client.utils.Values
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.burnoutcrew.reorderable.ItemPosition
import org.osmdroid.util.GeoPoint
import javax.inject.Inject

@SuppressLint("LongLogTag")

@HiltViewModel
class OrderExecutionViewModel @Inject constructor(
    application: Application,
    private val sendAddRatingUseCase: SendAddRatingUseCase,
    private val getActiveOrdersUseCase: GetActiveOrdersUseCase,
    private val cancelOrderUseCase: CancelOrderUseCase,
    private val editOrderUseCase: EditOrderUseCase,
    private val connectClientWithDriverUseCase: ConnectClientWithDriverUseCase,
    private val searchAddressUseCase: SearchAddressUseCase,
    private val getAddressByPointUseCase: GetAddressByPointUseCase,
    private val getReasonsUseCase: GetReasonsUseCase,
    private val getRatingReasonsUseCase: GetRatingReasonsUseCase,
    private val getAllowancesUseCase: GetAllowancesUseCase,
    private val getPriceUseCase: GetPriceUseCase
    ) : AndroidViewModel(application) {

    private var _selectedAllowances: MutableList<AllowanceRequest> =
        mutableListOf<AllowanceRequest>()
    val selectedAllowances = MutableLiveData<MutableList<AllowanceRequest>>(_selectedAllowances)
    val context get() = getApplication<Application>()
    private val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    private val mapController = MapController(context)

    private val _stateAddRating = mutableStateOf(AddRatingResponseState())
    val stateAddRating: State<AddRatingResponseState> = _stateAddRating

    private val _stateConnectClientWithDriver = mutableStateOf(ConnectClientWithDriverResponseState())
    val stateConnectClientWithDriver = _stateConnectClientWithDriver

    private val _stateActiveOrders = mutableStateOf(ActiveOrdersResponseState())
    val stateActiveOrders: State<ActiveOrdersResponseState> = _stateActiveOrders

    private val _stateActiveOrdersList = mutableStateListOf<AllActiveOrdersResult>()
    val stateActiveOrdersList: SnapshotStateList<AllActiveOrdersResult> = _stateActiveOrdersList


    private val _stateCancelOrder = mutableStateOf(CancelOrderResponseState())
    val stateCancelOrder: State<CancelOrderResponseState> = _stateCancelOrder

    private val _stateEditOrder = mutableStateOf(EditOrderResponseState())
    val stateEditOrder: State<EditOrderResponseState> = _stateEditOrder

    private val _stateAddressPoint = mutableStateOf(AddressByPointResponseState())
    val stateAddressPoint: State<AddressByPointResponseState> = _stateAddressPoint

    private val _selectedOrder = mutableStateOf(AllActiveOrdersResult())
    val selectedOrder: State<AllActiveOrdersResult> = _selectedOrder

    private var _toAddresses = mutableStateListOf<Address>()
    val toAddresses: SnapshotStateList<Address> = _toAddresses

    private val _fromAddress = mutableStateOf(Address("", 0, "", ""))
    val fromAddress: State<Address> = _fromAddress

    private val _stateSearchAddress = mutableStateOf(SearchAddressResponseState())
    val stateSearchAddress: State<SearchAddressResponseState> = _stateSearchAddress

    fun searchAddress(addressName: String) {
        searchAddressUseCase.invoke(if(addressName=="") null else addressName).onEach { result: Resource<SearchAddressResponse> ->
            when (result) {
                is Resource.Success -> {
                    try {
                        val addressResponse: SearchAddressResponse? = result.data
                        _stateSearchAddress.value =
                            SearchAddressResponseState(response = addressResponse?.result)
                        Log.e(
                            "SearchAddressResponse",
                            "SearchAddressResponseSuccess->\n ${_stateSearchAddress.value}"
                        )
                    } catch (e: Exception) {
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

    fun updateFromAddress(address: Address) {
        _fromAddress.value = address
        showRoad()
        Log.i("fromaDA", "" + _fromAddress.value)
    }

    fun updateToAddressInx(address: Address, inx: Int) {
        _toAddresses[inx] = address
        editOrder {

        }
    }

    fun updateToAddress(address: Address? = null, clear: Boolean = false) {
        if (clear) clearToAddress()
        if (address != null) {
            _toAddresses.add(address)
            _toAddresses[_toAddresses.lastIndex].idIncrement = _toAddresses.lastIndex
        }
        showRoad()
        Log.i("fromaDA", "" + _toAddresses.size)
    }

    fun clearAddresses() {
        _fromAddress.value = Address()
        map.overlays.clear()
    }

    fun addToAddress(address: Address) {
        _toAddresses.add(address)
        _toAddresses[_toAddresses.lastIndex].idIncrement = _toAddresses.lastIndex
        editOrder {

        }
        showRoad()
    }

    fun updateSelectedOrder(order: AllActiveOrdersResult) {
        _selectedOrder.value = order
    }

    fun sendRating2(
        order_id: Int,
        add_rating: Int,
        rating_reason: String
    ) {
        sendAddRatingUseCase.invoke(order_id, add_rating, if(rating_reason=="") null else rating_reason)
            .onEach { result: Resource<AddRatingResponse> ->
                when (result) {
                    is Resource.Success -> {
                        try {
                            val addressResponse: AddRatingResponse? = result.data
                            _stateAddRating.value =
                                AddRatingResponseState(response = addressResponse?.result)
                            Log.e(
                                "AddRatingResponse",
                                "AddRatingResponseSuccess->\n ${_stateAddRating.value}"
                            )
                        } catch (e: Exception) {
                            Log.d("AddRatingResponse", "${e.message} Exception")
                        }
                    }
                    is Resource.Error -> {
                        Log.e("AddRatingResponse", "AddRatingResponseError->\n ${result.message}")
                        _stateAddRating.value = AddRatingResponseState(
                            error = "${result.message}"
                        )
                    }
                    is Resource.Loading -> {
                        _stateAddRating.value = AddRatingResponseState(isLoading = true)
                    }
                }
            }.launchIn(viewModelScope)
    }

    fun connectClientWithDriver(
        order_id: String,
        onSuccess: () -> Unit
    ) {
        connectClientWithDriverUseCase.invoke(order_id)
            .onEach { result: Resource<connectClientWithDriverResponse> ->
                when (result) {
                    is Resource.Success -> {
                        try {
                            val connectClientWithDriverResponse: connectClientWithDriverResponse? =
                                result.data
                            _stateConnectClientWithDriver.value =
                                ConnectClientWithDriverResponseState(response = connectClientWithDriverResponse)
                            onSuccess()
                            Log.e(
                                "ConnectClientWithDriverResponseSuccess",
                                "ConnectClientWithDriverResponseSuccess->\n ${_stateConnectClientWithDriver.value.response}"
                            )
                        } catch (e: Exception) {
                            Log.d("Exception", "${e.message} Exception")
                        }
                    }
                    is Resource.Error -> {
                        Log.e(
                            "ConnectClientWithDriverResponse",
                            "ConnectClientWithDriverResponseError->\n ${result.message}"
                        )
                        _stateConnectClientWithDriver.value = ConnectClientWithDriverResponseState(
                            error = "${result.message}"
                        )
                    }
                    is Resource.Loading -> {
                        _stateConnectClientWithDriver.value =
                            ConnectClientWithDriverResponseState(isLoading = true)
                    }
                }
            }.launchIn(viewModelScope)
    }

    fun getActiveOrders(onSuccess: () -> Unit = {}) {
        getActiveOrdersUseCase.invoke().onEach { result: Resource<ActiveOrdersResponse> ->
            when (result) {
                is Resource.Success -> {
                    try {
                        val response: ActiveOrdersResponse? = result.data
                        _stateActiveOrders.value = ActiveOrdersResponseState(
                            response = response?.result,
                            success = true,
                            code = response?.code
                        )
                        _stateActiveOrdersList.clear()
                        stateActiveOrders.value.response?.forEach {
                            _stateActiveOrdersList.add(it)
                            if(it.id == selectedOrder.value.id){
                                updateSelectedOrder(it)
                            }
                        }

                        Log.e(
                            "ActiveOrdersResponse",
                            "ActiveOrdersResponseSuccess->\n ${_stateActiveOrders.value}"
                        )
                        showRoad()
                        onSuccess()
                    } catch (e: Exception) {
                        Log.d("ActiveOrdersResponse", "${e.message} Exception")
                    }
                }
                is Resource.Error -> {
                    Log.e("ActiveOrdersResponse", "ActiveOrdersResponseError->\n ${result.message}")
                    _stateActiveOrders.value = ActiveOrdersResponseState(
                        error = "${result.message}"
                    )
                }
                is Resource.Loading -> {
                    _stateActiveOrders.value = ActiveOrdersResponseState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun addActiveOrder(element: AllActiveOrdersResult){
        _stateActiveOrdersList.forEachIndexed{inx, it ->
            if(it.id == element.id){
                _stateActiveOrdersList[inx] = element
                return@forEachIndexed
            }else{
                _stateActiveOrdersList.add(element)
            }
        }
    }
    fun deleteActiveOrder(id: Int){
        _stateActiveOrdersList.forEachIndexed{inx, it ->
            if(it.id == id){
                _stateActiveOrdersList.removeAt(inx)
                return@forEachIndexed
            }
        }
    }


    fun cancelOrder(order_id: Int, reason_cancel_order: String, onSuccess: () -> Unit) {
        cancelOrderUseCase.invoke(order_id, reason_cancel_order).onEach { result: Resource<CancelOrderResponse> ->
            when (result) {
                is Resource.Success -> {
                    try {
                        getActiveOrders()
                        Toast.makeText(context, "Заказ успешно отменен", Toast.LENGTH_LONG).show()
                        vibrator.vibrate(700)
                        val response: CancelOrderResponse? = result.data
                        _stateCancelOrder.value =
                            CancelOrderResponseState(response = response)
                        //getActiveOrders()
//                        _stateActiveOrdersList.forEachIndexed { index, item ->
//                            if(item.id == order_id){
//                                _stateActiveOrdersList.removeAt(index)
//                                return@forEachIndexed
//                            }
//                        }
                        onSuccess()
                        Log.e(
                            "CancelOrderResponse",
                            "CancelOrderResponseSuccess->\n ${_stateCancelOrder.value}"
                        )
                    } catch (e: Exception) {
                        Log.d("CancelOrderResponse", "${e.message} Exception")
                    }
                }
                is Resource.Error -> {
                    Log.e("CancelOrderResponse", "CancelOrderResponseError->\n ${result.message}")
                    _stateCancelOrder.value = CancelOrderResponseState(
                        error = "${result.message}"
                    )
                }
                is Resource.Loading -> {
                    _stateCancelOrder.value = CancelOrderResponseState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun move(from: ItemPosition, to: ItemPosition) {
        _toAddresses = _toAddresses.apply {
            add(to.index, removeAt(from.index))
        }
        //editOrder(_toAddresses)
    }

    fun showRoad() {
        map.overlays.clear()

        selectedOrder.value.from_address?.let {
            if(it.lat != "" && (selectedOrder.value.to_addresses?.size ?: 0) > 0){
                if(Values.currentRoute.value == Routes.SEARCH_ADDRESS_SHEET || Values.currentRoute.value == Routes.MAP_POINT_SHEET || Values.currentRoute.value == Routes.SEARCH_DRIVER_SHEET) { return }
                map.controller.animateTo(GeoPoint(it.lat.toDouble(), it.lng.toDouble()), 16.0, 1000)
            }
            mapController.showRoadAB(
                it,
                selectedOrder.value.to_addresses
            )
        }
    }

    fun clearToAddress() {
        _toAddresses.clear()
    }

    fun removeAddStop(address: Address?) {
        _toAddresses.remove(address)
        editOrder {

        }
    }

    fun getAddressFromMap(
        lng: Double,
        lat: Double,
    ) {
        getAddressByPointUseCase.invoke(lng, lat)
            .onEach { result: Resource<AddressByPointResponse> ->
                when (result) {
                    is Resource.Success -> {
                        try {
                            val addressResponse: AddressByPointResponse? = result.data
                            _stateAddressPoint.value =
                                AddressByPointResponseState(response = addressResponse?.result)
                            Log.e(
                                "AddressByPointResponse",
                                "AddressByPointResponseSuccess->\n ${_stateAddressPoint.value}"
                            )
                            when (Values.WhichAddress.value) {
                                Constants.FROM_ADDRESS -> {
                                    updateFromAddress(
                                        _stateAddressPoint.value.response!!
                                    )
                                }
                                Constants.TO_ADDRESS -> {
                                    clearToAddress()
                                    addToAddress(
                                        _stateAddressPoint.value.response!!
                                    )
                                }
                            }
                            Log.e("singleTapConfirmedHelper", "${toAddresses.size}")

                        } catch (e: Exception) {
                            Log.d("AddressByPointResponse", "${e.message} Exception")
                        }
                    }
                    is Resource.Error -> {
                        Log.e(
                            "AddressByPointResponse",
                            "AddressByPointResponseError->\n ${result.message}"
                        )
                        if (result.message == "HTTP 404 Not Found") {
                            updateFromAddress(
                                Address(
                                    "Метка на карте",
                                    -1,
                                    map.mapCenter.latitude.toString(),
                                    map.mapCenter.longitude.toString()
                                )
                            )
                        }
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

    fun editOrder(onSuccess: () -> Unit) {
        editOrderUseCase.invoke(
            order_id = selectedOrder.value.id,
            dop_phone = null,
            search_address_id = selectedOrder.value.search_address_id,
            meeting_info = null,
            to_addresses = _toAddresses,
            comment = null,
            tariff_id = selectedOrder.value.tariff_id ?: 0,
            allowances = if (selectedAllowances.value?.isNotEmpty() == true) Gson().toJson(
                selectedAllowances.value
            ) else "[]",
            from_address = if (_fromAddress.value.id==-1) "{\"lng\":\"${fromAddress.value.lng}\",\"lat\":\"${fromAddress.value.lat}\"}" else null
        ).onEach { result: Resource<UpdateOrderResponse> ->
            when (result) {
                is Resource.Success -> {
                    try {
                        val response: UpdateOrderResponse? = result.data
                        _stateEditOrder.value =
                            EditOrderResponseState(response = response)
                        //readAllOrders()
                        Log.e(
                            "EditOrderResponse",
                            "EditOrderResponseSuccess->\n ${_stateEditOrder.value}"
                        )
                        onSuccess()
                    } catch (e: Exception) {
                        Log.d("EditOrderResponse", "${e.message} Exception")
                    }
                }
                is Resource.Error -> {
                    Log.e("EditOrderResponse", "EditOrderResponseError->\n ${result.message}")
                    _stateEditOrder.value = EditOrderResponseState(
                        error = "${result.message}"
                    )
                }
                is Resource.Loading -> {
                    _stateEditOrder.value = EditOrderResponseState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }


    private val _stateCalculate = mutableStateOf(CalculateResponseState())
    val stateCalculate: State<CalculateResponseState> = _stateCalculate
    fun getPrice() {
        val tariffIdsList = arrayListOf<String>()
        tariffIdsList.add("{\"tariff_id\":${selectedOrder.value.tariff_id}}")
        getPriceUseCase.invoke(
            tariff_ids = tariffIdsList.toString(),
            allowances = if (selectedAllowances.value?.isNotEmpty() == true) Gson().toJson(
                selectedAllowances.value
            ) else "[]",
            search_address_id = selectedOrder.value.search_address_id,
            to_addresses = if (selectedOrder.value.to_addresses?.isNotEmpty() == true) toAddresses else null,
            from_address = if ((selectedOrder.value.from_address != null)) "{\"lng\":\"${selectedOrder.value.from_address!!.lng}\",\"lat\":\"${selectedOrder.value.from_address!!.lat}\"}" else null
        ).onEach { result: Resource<CalculateResponse> ->
            when (result) {
                is Resource.Success -> {
                    try {
                        val response: CalculateResponse? = result.data
                        _stateCalculate.value =
                            CalculateResponseState(response = response)
                        Log.e(
                            "CalculateResponse",
                            "CalculateResponseSuccess->\n ${_stateCalculate.value}"
                        )
                    } catch (e: Exception) {
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

    fun getDriverLocation(orderId: Int) {
        val postReference = Firebase.database.reference
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try {
                    val geoPoint = GeoPoint(0.0, 0.0)
                    geoPoint.latitude =
                        dataSnapshot.child("performer_locations/$orderId/lat").value.toString()
                            .toDouble()
                    geoPoint.longitude =
                        dataSnapshot.child("performer_locations/$orderId/lng").value.toString()
                            .toDouble()
                    Values.DriverLocation.value = geoPoint
                    Log.i("adafas", "change = " + Values.DriverLocation.value)
                } catch (e: Exception) {
                    Values.DriverLocation.value = GeoPoint(0.0, 0.0)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }
        postReference.addValueEventListener(postListener)
    }

    private val _stateGetReasons = mutableStateOf(GetReasonsResponseState())
    val stateGetReasons: State<GetReasonsResponseState> = _stateGetReasons

    fun getReasons() {
        getReasonsUseCase.invoke().onEach { result: Resource<Reasons> ->
            when (result) {
                is Resource.Success -> {
                    try {
                        val tariffsResponse: Reasons? = result.data
                        _stateGetReasons.value =
                            GetReasonsResponseState(response = tariffsResponse)
                        Log.e("GetReasonsResponse", "GetReasonsResponseSuccess->\n${result.data}")
                    } catch (e: Exception) {
                        Log.d("Exception", "${e.message} Exception")
                    }
                }
                is Resource.Error -> {
                    Log.e("GetReasonsResponse", "GetReasonsResponseError->\n ${result.message}")
                    _stateGetReasons.value = GetReasonsResponseState(
                        error = "${result.message}"
                    )
                }
                is Resource.Loading -> {
                    _stateGetReasons.value = GetReasonsResponseState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    private val _stateGetRatingReasons = mutableStateOf(GetRatingReasonsResponseState())
    val stateGetRatingReasons: State<GetRatingReasonsResponseState> = _stateGetRatingReasons

    fun getRatingReasons() {
        getRatingReasonsUseCase.invoke().onEach { result: Resource<GetRatingReasonsResponse> ->
            when (result) {
                is Resource.Success -> {
                    try {
                        val tariffsResponse: GetRatingReasonsResponse? = result.data
                        _stateGetRatingReasons.value =
                            GetRatingReasonsResponseState(response = tariffsResponse)
                        Log.e("GetRatingReasonsResponseState", "GetRatingReasonsResponseStateSuccess->\n${result.data}")
                    } catch (e: Exception) {
                        Log.d("Exception", "${e.message} Exception")
                    }
                }
                is Resource.Error -> {
                    Log.e("GetRatingReasonsResponseState", "GetRatingReasonsResponseStateError->\n ${result.message}")
                    _stateGetRatingReasons.value = GetRatingReasonsResponseState(
                        error = "${result.message}"
                    )
                }
                is Resource.Loading -> {
                    _stateGetRatingReasons.value = GetRatingReasonsResponseState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
    private val _stateAllowances = mutableStateOf(AllowancesResponseState())
    val stateAllowances: State<AllowancesResponseState> = _stateAllowances
    fun getAllowancesByTariffId(tariff_id: Int, onSuccess: () -> Unit) {
        getAllowancesUseCase.invoke(tariff_id = tariff_id)
            .onEach { result: Resource<AllowancesResponse> ->
                when (result) {
                    is Resource.Success -> {
                        try {
                            val allowancesResponse: AllowancesResponse? = result.data
                            _stateAllowances.value =
                                AllowancesResponseState(response = allowancesResponse?.result?.map { it.toDesiredAllowance() })
                            Log.e(
                                "AllowancesResponse",
                                "AllowancesResponseSuccess->\n ${_stateAllowances.value}"
                            )
                            onSuccess()
                        } catch (e: Exception) {
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
    fun includeAllowance(toDesiredAllowance: ToDesiredAllowance, price: Int? = null) {
        Log.i("include", ""+toDesiredAllowance)
        selectedAllowances.value?.forEachIndexed { inx, it ->
            if (it.allowance_id == toDesiredAllowance.id) {
                _selectedAllowances.removeAt(inx)
                if (toDesiredAllowance.isSelected.value) {
                    _selectedAllowances.add(toDesiredAllowance.toAllowanceRequest(price))
                    Log.i("asdad", ""+_selectedAllowances)
                }
                selectedAllowances.value = _selectedAllowances
                return
            }
        }
        if (toDesiredAllowance.isSelected.value) {
            _selectedAllowances.add(toDesiredAllowance.toAllowanceRequest(price))
            selectedAllowances.value = _selectedAllowances
            Log.i("asdad", ""+_selectedAllowances)

        } else if(price!=null) {
            _selectedAllowances.remove(toDesiredAllowance.toAllowanceRequest(price))
            selectedAllowances.value = _selectedAllowances
        }
    }
    fun clearSelectedAllowance() {
        _selectedAllowances.clear()
    }
    fun clearCalculate() {
        _stateCalculate.value = CalculateResponseState(response = null)
    }

}