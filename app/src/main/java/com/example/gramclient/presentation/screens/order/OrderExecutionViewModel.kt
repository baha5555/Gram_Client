package com.example.gramclient.presentation.screens.order

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
import com.example.gramclient.domain.mainScreen.order.*
import com.example.gramclient.domain.mainScreen.order.connectClientWithDriver.connectClientWithDriverResponse
import com.example.gramclient.domain.orderExecutionScreen.*
import com.example.gramclient.domain.firebase.order.RealtimeDatabaseOrder
import com.example.gramclient.domain.firebase.GetClientOrderUseCase
import com.example.gramclient.domain.firebase.GetOrdersUseCase
import com.example.gramclient.domain.firebase.profile.Client
import com.example.gramclient.domain.mainScreen.Address
import com.example.gramclient.domain.mainScreen.SearchAddressResponse
import com.example.gramclient.domain.mainScreen.SearchAddressUseCase
import com.example.gramclient.presentation.screens.order.states.GetClientOrderState
import com.example.gramclient.presentation.screens.order.states.GetOrdersState
import com.example.gramclient.presentation.screens.main.states.CancelOrderResponseState
import com.example.gramclient.presentation.screens.main.states.SearchAddressResponseState
import com.example.gramclient.presentation.screens.map.MapController
import com.example.gramclient.presentation.screens.map.map
import com.example.gramclient.presentation.screens.map.showRoadAB
import com.example.gramclient.utils.Resource
import com.example.gramclient.utils.Values
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.burnoutcrew.reorderable.ItemPosition
import org.osmdroid.util.GeoPoint
import javax.inject.Inject

@HiltViewModel
class OrderExecutionViewModel  @Inject constructor(
    application: Application,
    private val sendAddRatingUseCase: SendAddRatingUseCase,
    private val getActiveOrdersUseCase: GetActiveOrdersUseCase,
    private val cancelOrderUseCase: CancelOrderUseCase,
    private val editOrderUseCase: EditOrderUseCase,
    private val getOrdersUseCase: GetOrdersUseCase,
    private val getClientOrderUseCase: GetClientOrderUseCase,
    private val connectClientWithDriverUseCase: ConnectClientWithDriverUseCase,
    private val searchAddressUseCase: SearchAddressUseCase
): AndroidViewModel(application) {
    val context get() = getApplication<Application>()
    private val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    val mapController = MapController(context)

    private val _stateAddRating = mutableStateOf(AddRatingResponseState())
    val stateAddRating: State<AddRatingResponseState> = _stateAddRating

    private val _stateConnectClientWithDriver = mutableStateOf(ConnectClientWithDriverResponseState())
    val stateConnectClientWithDriver = _stateConnectClientWithDriver
    private val _stateRealtimeOrdersDatabase = mutableStateOf(GetOrdersState())
    val stateRealtimeOrdersDatabase: State<GetOrdersState> = _stateRealtimeOrdersDatabase

    private val _stateRealtimeClientOrderIdDatabase = mutableStateOf(GetClientOrderState())
    val stateRealtimeClientOrderIdDatabase: State<GetClientOrderState> = _stateRealtimeClientOrderIdDatabase

    private val _stateActiveOrders = mutableStateOf(ActiveOrdersResponseState())
    val stateActiveOrders: State<ActiveOrdersResponseState> = _stateActiveOrders

    private val _stateCancelOrder = mutableStateOf(CancelOrderResponseState())
    val stateCancelOrder: State<CancelOrderResponseState> = _stateCancelOrder

    private val _stateEditOrder = mutableStateOf(EditOrderResponseState())
    val stateEditOrder: State<EditOrderResponseState> = _stateEditOrder

    private val _selectedOrder = mutableStateOf(RealtimeDatabaseOrder())
    val selectedOrder: State<RealtimeDatabaseOrder> = _selectedOrder

    private var _toAddresses = mutableStateListOf<Address>()
    val toAddresses: SnapshotStateList<Address> = _toAddresses

    private val _fromAddress = mutableStateOf(Address("", 0, "", ""))
    val fromAddress: State<Address> = _fromAddress

    private val _stateSearchAddress = mutableStateOf(SearchAddressResponseState())
    val stateSearchAddress: State<SearchAddressResponseState> = _stateSearchAddress

    fun searchAddress(addressName: String) {
        searchAddressUseCase.invoke(addressName).onEach { result: Resource<SearchAddressResponse> ->
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

    fun updateFromAddress(address: Address){
        _fromAddress.value=address
        mapController.showRoadAB(_fromAddress, _toAddresses)
        Log.i("fromaDA", ""+_fromAddress.value)
    }

    fun updateToAddressInx(address: Address, inx: Int) {
        _toAddresses[inx] = address
        editOrder(_toAddresses)
    }

    fun updateToAddress(address: Address? = null, clear: Boolean = false){
        if(clear) _toAddresses.clear()
        if (address != null) {
            _toAddresses.add(address)
        }
        mapController.showRoadAB(_fromAddress, _toAddresses)
        Log.i("fromaDA", ""+_toAddresses.size)
    }
    fun clearAddresses(){
        _fromAddress.value=Address()
        map.overlays.clear()
    }
    fun addToAddress(address: Address){
        if (_toAddresses.contains(address)) return
        _toAddresses.add(address)
        editOrder(_toAddresses)
        mapController.showRoadAB(_fromAddress, _toAddresses)
    }

    fun updateSelectedOrder(order: RealtimeDatabaseOrder) {
        _selectedOrder.value = order
    }

    fun readAllOrders() {
            getOrdersUseCase.invoke().onEach { result: Resource<LiveData<List<RealtimeDatabaseOrder>>> ->
                when (result){
                    is Resource.Success -> {
                        try {
                            val addressResponse: LiveData<List<RealtimeDatabaseOrder>>? = result.data

                                _stateRealtimeOrdersDatabase.value =
                                    GetOrdersState(response = addressResponse)
                            Log.e("readAllOrdersFromRealtimeResponse", "Success->\n ${_stateRealtimeOrdersDatabase.value.response?.value}")
                        }catch (e: Exception) {
                            Log.d("readAllOrdersFromRealtimeResponse", "${e.message} Exception")
                        }
                    }
                    else -> {}
                }
            }.launchIn(viewModelScope)
    }
    fun readAllClient(client:String) {
        if(client=="")return
        getClientOrderUseCase.invoke(client).onEach { result: Resource<LiveData<Client>> ->
            when (result){
                is Resource.Success -> {
                    try {
                        val realtimeClientOrderIdDatabaseResponseResponse: LiveData<Client>? = result.data

                        _stateRealtimeClientOrderIdDatabase.value =
                            GetClientOrderState(response = realtimeClientOrderIdDatabaseResponseResponse)

                        Log.e("RealtimeClientOrderIdDatabaseResponse", "Success->\n ${_stateRealtimeClientOrderIdDatabase.value.response?.value }")
                    }catch (e: Exception) {
                        Log.d("Exception", "${e.message} Exception")
                    }
                }
                else -> {}
            }
        }.launchIn(viewModelScope)
    }

    fun sendRating2(
      order_id: Int,
      add_rating: Int){
        sendAddRatingUseCase.invoke(order_id, add_rating).onEach { result: Resource<AddRatingResponse> ->
            when (result){
                is Resource.Success -> {
                    try {
                        val addressResponse: AddRatingResponse? = result.data
                        _stateAddRating.value =
                            AddRatingResponseState(response = addressResponse?.result)
                        Log.e("AddRatingResponse", "AddRatingResponseSuccess->\n ${_stateAddRating.value}")
                    }catch (e: Exception) {
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
    ){
        connectClientWithDriverUseCase.invoke(order_id).onEach { result: Resource<connectClientWithDriverResponse> ->
            when (result){
                is Resource.Success -> {
                    try {
                        val connectClientWithDriverResponse: connectClientWithDriverResponse? = result.data
                        _stateConnectClientWithDriver.value =
                            ConnectClientWithDriverResponseState(response = connectClientWithDriverResponse)
                        onSuccess()
                        Log.e("ConnectClientWithDriverResponseSuccess", "ConnectClientWithDriverResponseSuccess->\n ${_stateConnectClientWithDriver.value.response}")
                    }catch (e: Exception) {
                        Log.d("Exception", "${e.message} Exception")
                    }
                }
                is Resource.Error -> {
                    Log.e("ConnectClientWithDriverResponse", "ConnectClientWithDriverResponseError->\n ${result.message}")
                    _stateConnectClientWithDriver.value = ConnectClientWithDriverResponseState(
                        error = "${result.message}"
                    )
                }
                is Resource.Loading -> {
                    _stateConnectClientWithDriver.value = ConnectClientWithDriverResponseState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getActiveOrders(){
        getActiveOrdersUseCase.invoke().onEach { result: Resource<ActiveOrdersResponse> ->
            when (result){
                is Resource.Success -> {
                    try {
                        val response: ActiveOrdersResponse? = result.data
                        _stateActiveOrders.value = ActiveOrdersResponseState(response = response?.result, success = true)
                        Log.e("ActiveOrdersResponse", "ActiveOrdersResponseSuccess->\n ${_stateActiveOrders.value}")
                    }catch (e: Exception) {
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

    fun cancelOrder(order_id: Int, onSuccess:()->Unit){
        cancelOrderUseCase.invoke(order_id).onEach { result: Resource<CancelOrderResponse> ->
            when (result){
                is Resource.Success -> {
                    try {
                        Toast.makeText(context, "Заказ успешно отменен", Toast.LENGTH_LONG).show()
                        vibrator.vibrate(700)
                        val response: CancelOrderResponse? = result.data
                        _stateCancelOrder.value =
                            CancelOrderResponseState(response = response)
                        onSuccess()
                        getActiveOrders()
                        Log.e("CancelOrderResponse", "CancelOrderResponseSuccess->\n ${_stateCancelOrder.value}")
                    }catch (e: Exception) {
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
        editOrder(_toAddresses)
    }

    fun showRoad(){
        map.overlays.clear()
        showRoadAB(
            context,
            _fromAddress,
            _toAddresses
        )
    }

    fun removeAddStop(address: Address?) {
        _toAddresses.remove(address)
        editOrder(_toAddresses)
    }
    fun editOrder(_toAddresses: SnapshotStateList<Address>) {
        editOrderUseCase.invoke(
            order_id = selectedOrder.value.id,
            dop_phone = null,
            from_address = selectedOrder.value.from_address?.id,
            meeting_info = null,
            to_addresses = _toAddresses,
            comment = null,
            tariff_id = selectedOrder.value.tariff_id?:0,
            allowances = if(selectedOrder.value.allowances!=null) Gson().toJson(selectedOrder.value.allowances) else null
        ).onEach { result: Resource<UpdateOrderResponse> ->
            when (result){
                is Resource.Success -> {
                    try {
                        val response: UpdateOrderResponse? = result.data
                        _stateEditOrder.value =
                            EditOrderResponseState(response = response)
                        //readAllOrders()
                        Log.e("EditOrderResponse", "EditOrderResponseSuccess->\n ${_stateEditOrder.value}")
                    }catch (e: Exception) {
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

    fun getDriverLocation(orderId:Int){
        val postReference = Firebase.database.reference
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try {
                    val geoPoint = GeoPoint(0.0, 0.0)
                    geoPoint.latitude = dataSnapshot.child("performer_locations/$orderId/lat").value.toString().toDouble()
                    geoPoint.longitude = dataSnapshot.child("performer_locations/$orderId/lng").value.toString().toDouble()
                    Values.DriverLocation.value=geoPoint
                    Log.i("adafas", "change = "+Values.DriverLocation.value)
                } catch (e: Exception) {
                    Values.DriverLocation.value = GeoPoint(0.0, 0.0)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        }
        postReference.addValueEventListener(postListener)
    }
}