package com.gram.client.presentation.screens.order

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
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.gram.client.domain.mainScreen.order.*
import com.gram.client.domain.mainScreen.order.connectClientWithDriver.connectClientWithDriverResponse
import com.gram.client.domain.orderExecutionScreen.*
import com.gram.client.domain.firebase.order.RealtimeDatabaseOrder
import com.gram.client.domain.firebase.GetClientOrderUseCase
import com.gram.client.domain.firebase.GetOrdersUseCase
import com.gram.client.domain.firebase.profile.Client
import com.gram.client.domain.mainScreen.*
import com.gram.client.presentation.screens.main.states.AddressByPointResponseState
import com.gram.client.presentation.screens.order.states.GetClientOrderState
import com.gram.client.presentation.screens.order.states.GetOrdersState
import com.gram.client.presentation.screens.main.states.CancelOrderResponseState
import com.gram.client.presentation.screens.main.states.SearchAddressResponseState
import com.gram.client.presentation.screens.map.MapController
import com.gram.client.presentation.screens.map.map
import com.gram.client.presentation.screens.order.states.GetReasonsResponseState
import com.gram.client.utils.Constants
import com.gram.client.utils.Resource
import com.gram.client.utils.Values
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.gram.client.domain.orderExecutionScreen.active.ActiveOrdersResponse
import com.gram.client.domain.orderExecutionScreen.active.AllActiveOrdersResult
import com.gram.client.domain.orderExecutionScreen.reason.*
import com.gram.client.presentation.screens.order.states.GetRatingReasonsResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.burnoutcrew.reorderable.ItemPosition
import org.osmdroid.util.GeoPoint
import javax.inject.Inject

@HiltViewModel
class OrderExecutionViewModel @Inject constructor(
    application: Application,
    private val sendAddRatingUseCase: SendAddRatingUseCase,
    private val getActiveOrdersUseCase: GetActiveOrdersUseCase,
    private val cancelOrderUseCase: CancelOrderUseCase,
    private val editOrderUseCase: EditOrderUseCase,
    private val getOrdersUseCase: GetOrdersUseCase,
    private val getClientOrderUseCase: GetClientOrderUseCase,
    private val connectClientWithDriverUseCase: ConnectClientWithDriverUseCase,
    private val searchAddressUseCase: SearchAddressUseCase,
    private val getAddressByPointUseCase: GetAddressByPointUseCase,
    private val getReasonsUseCase: GetReasonsUseCase,
    private val getRatingReasonsUseCase: GetRatingReasonsUseCase
    ) : AndroidViewModel(application) {
    val context get() = getApplication<Application>()
    private val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    private val mapController = MapController(context)

    private val _stateAddRating = mutableStateOf(AddRatingResponseState())
    val stateAddRating: State<AddRatingResponseState> = _stateAddRating

    private val _stateConnectClientWithDriver =
        mutableStateOf(ConnectClientWithDriverResponseState())
    val stateConnectClientWithDriver = _stateConnectClientWithDriver
    private val _stateRealtimeOrdersDatabase = mutableStateOf(GetOrdersState())
    val stateRealtimeOrdersDatabase: State<GetOrdersState> = _stateRealtimeOrdersDatabase

    private val _stateRealtimeClientOrderIdDatabase = mutableStateOf(GetClientOrderState())
    val stateRealtimeClientOrderIdDatabase: State<GetClientOrderState> =
        _stateRealtimeClientOrderIdDatabase

    private val _stateActiveOrders = mutableStateOf(ActiveOrdersResponseState())
    val stateActiveOrders: State<ActiveOrdersResponseState> = _stateActiveOrders

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
        editOrder()
    }

    fun updateToAddress(address: Address? = null, clear: Boolean = false) {
        if (clear) _toAddresses.clear()
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
        editOrder()
        showRoad()
    }

    fun updateSelectedOrder(order: AllActiveOrdersResult) {
        _selectedOrder.value = order
    }

    fun readAllClient(client: String) {
        if (client == "") return
        getClientOrderUseCase.invoke(client).onEach { result: Resource<LiveData<Client>> ->
            when (result) {
                is Resource.Success -> {
                    try {
                        val realtimeClientOrderIdDatabaseResponseResponse: LiveData<Client>? =
                            result.data

                        _stateRealtimeClientOrderIdDatabase.value =
                            GetClientOrderState(response = realtimeClientOrderIdDatabaseResponseResponse)

                        Log.e(
                            "RealtimeClientOrderIdDatabaseResponse",
                            "Success->\n ${_stateRealtimeClientOrderIdDatabase.value.response?.value}"
                        )
                    } catch (e: Exception) {
                        Log.d("Exception", "${e.message} Exception")
                    }
                }
                else -> {}
            }
        }.launchIn(viewModelScope)
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
                        Log.e(
                            "ActiveOrdersResponse",
                            "ActiveOrdersResponseSuccess->\n ${_stateActiveOrders.value}"
                        )
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

    fun cancelOrder(order_id: Int, reason_cancel_order: String, onSuccess: () -> Unit) {
        cancelOrderUseCase.invoke(order_id, reason_cancel_order).onEach { result: Resource<CancelOrderResponse> ->
            when (result) {
                is Resource.Success -> {
                    try {
                        Toast.makeText(context, "Заказ успешно отменен", Toast.LENGTH_LONG).show()
                        vibrator.vibrate(700)
                        val response: CancelOrderResponse? = result.data
                        _stateCancelOrder.value =
                            CancelOrderResponseState(response = response)
                        onSuccess()
                        getActiveOrders()
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
        mapController.showRoadAB(
            _fromAddress,
            _toAddresses
        )
    }

    fun clearToAddress() {
        _toAddresses.clear()
    }

    fun removeAddStop(address: Address?) {
        _toAddresses.remove(address)
        editOrder()
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
                                        Address(
                                            _stateAddressPoint.value.response!!.name,
                                            _stateAddressPoint.value.response!!.id,
                                            _stateAddressPoint.value.response!!.lat,
                                            _stateAddressPoint.value.response!!.lng
                                        )
                                    )
                                }
                                Constants.TO_ADDRESS -> {
                                    clearToAddress()
                                    addToAddress(
                                        Address(
                                            _stateAddressPoint.value.response!!.name,
                                            _stateAddressPoint.value.response!!.id,
                                            _stateAddressPoint.value.response!!.lat,
                                            _stateAddressPoint.value.response!!.lng
                                        )
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

    fun editOrder() {
        editOrderUseCase.invoke(
            order_id = selectedOrder.value.id,
            dop_phone = null,
            search_address_id = if (_fromAddress.value.id!=-1) _fromAddress.value.id else null,
            meeting_info = null,
            to_addresses = _toAddresses,
            comment = null,
            tariff_id = selectedOrder.value.tariff_id ?: 0,
            allowances = null,
            from_address = if (_fromAddress.value.id==-1) "{\"lng\":\"${fromAddress.value.address_lng}\",\"lat\":\"${fromAddress.value.address_lat}\"}" else null
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
}