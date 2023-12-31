package com.gram.client.presentation.screens.drawer.myaddresses_screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gram.client.domain.myAddresses.*
import com.gram.client.utils.Resource
import com.gram.client.presentation.screens.drawer.myaddresses_screen.state.AddMyAddressesResponseState
import com.gram.client.presentation.screens.drawer.myaddresses_screen.state.DeleteMyAddressesResponseState
import com.gram.client.presentation.screens.drawer.myaddresses_screen.state.GetAllMyAddressesResponseState
import com.gram.client.presentation.screens.drawer.myaddresses_screen.state.UpdateMyAddressesResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MyAddressViewModel @Inject constructor(
    private val myAddressesUseCase: MyAddressesUseCase
) : ViewModel() {
    private val _stateAddMyAddress = mutableStateOf(AddMyAddressesResponseState())
    val stateAddMyAddress: State<AddMyAddressesResponseState> = _stateAddMyAddress

    fun addMyAddress(
        addMyAddressRequest: AddMyAddressRequest,
        success: () -> Boolean
    ) {
        myAddressesUseCase.addMyAddresses(
            addMyAddressRequest = addMyAddressRequest
        ).onEach { result: Resource<AddMyAddressesResponse> ->
            when (result) {
                is Resource.Success -> {
                    try {
                        val tariffsResponse: AddMyAddressesResponse? = result.data
                        _stateAddMyAddress.value =
                            AddMyAddressesResponseState(response = tariffsResponse)
                        success.invoke()
                        Log.e(
                            "AddMyAddressesResponse",
                            "AddMyAddressesResponseSuccess->\n ${_stateAddMyAddress.value}"
                        )
                    } catch (e: Exception) {
                        Log.d("Exception", "${e.message} Exception")
                    }
                }
                is Resource.Error -> {
                    Log.e(
                        "AddMyAddressesResponse",
                        "AddMyAddressesResponseError->\n ${result.message}"
                    )
                    _stateAddMyAddress.value = AddMyAddressesResponseState(
                        error = "${result.message}"
                    )
                }
                is Resource.Loading -> {
                    _stateAddMyAddress.value = AddMyAddressesResponseState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    private val _stateGetAllMyAddresses = mutableStateOf(GetAllMyAddressesResponseState())
    val stateGetAllMyAddresses: State<GetAllMyAddressesResponseState> = _stateGetAllMyAddresses

    @SuppressLint("LongLogTag")
    fun getAllMyAddresses() {
        myAddressesUseCase.getAllMyAddresses()
            .onEach { result: Resource<GetAllMyAddressesResponse> ->
                when (result) {
                    is Resource.Success -> {
                        try {
                            val response: GetAllMyAddressesResponse? = result.data
                            _stateGetAllMyAddresses.value =
                                GetAllMyAddressesResponseState(response = response)
                            Log.e(
                                "GetAllMyAddressesResponse",
                                "GetAllMyAddressesResponseSuccess->\n ${_stateGetAllMyAddresses.value}"
                            )
                        } catch (e: Exception) {
                            Log.d("Exception", "${e.message} Exception")
                        }
                    }
                    is Resource.Error -> {
                        Log.e(
                            "GetAllMyAddressesResponse",
                            "GetAllMyAddressesResponseError->\n ${result.message}"
                        )
                        _stateGetAllMyAddresses.value = GetAllMyAddressesResponseState(
                            error = "${result.message}"
                        )
                    }
                    is Resource.Loading -> {
                        _stateGetAllMyAddresses.value =
                            GetAllMyAddressesResponseState(isLoading = true)
                    }
                }
            }.launchIn(viewModelScope)
    }

    private val _stateUpdateMyAddress = mutableStateOf(UpdateMyAddressesResponseState())
    val stateUpdateMyAddress: State<UpdateMyAddressesResponseState> = _stateUpdateMyAddress

    fun updateMyAddress(
        updateMyAddressRequest: UpdateMyAddressRequest,
        success: () -> Boolean
    ) {
        myAddressesUseCase.updateMyAddresses(updateMyAddressRequest = updateMyAddressRequest).onEach { result: Resource<UpdateMyAddressResponse> ->
            when (result) {
                is Resource.Success -> {
                    try {
                        val tariffsResponse: UpdateMyAddressResponse? = result.data
                        _stateUpdateMyAddress.value =
                            UpdateMyAddressesResponseState(response = tariffsResponse)
                        success.invoke()
                        Log.e(
                            "UpdateMyAddressesResponse",
                            "UpdateMyAddressesResponseSuccess->\n ${_stateUpdateMyAddress.value}"
                        )
                    } catch (e: Exception) {
                        Log.d("Exception", "${e.message} Exception")
                    }
                }
                is Resource.Error -> {
                    Log.e(
                        "UpdateMyAddressesResponse",
                        "UpdateMyAddressesResponseError->\n ${result.message}"
                    )
                    _stateUpdateMyAddress.value = UpdateMyAddressesResponseState(
                        error = "${result.message}"
                    )
                }
                is Resource.Loading -> {
                    _stateUpdateMyAddress.value = UpdateMyAddressesResponseState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    private val _stateDeleteMyAddress = mutableStateOf(DeleteMyAddressesResponseState())
    val stateUpdateMyAddress2: State<DeleteMyAddressesResponseState> = _stateDeleteMyAddress

    fun deleteMyAddress(
        id: Int,
        success: () -> Boolean
    ) {
        myAddressesUseCase.deleteMyAddresses(id)
            .onEach { result: Resource<DeleteMyAddressesResponse> ->
                when (result) {
                    is Resource.Success -> {
                        try {
                            val tariffsResponse: DeleteMyAddressesResponse? = result.data
                            _stateDeleteMyAddress.value =
                                DeleteMyAddressesResponseState(response = tariffsResponse)
                            success.invoke()
                            Log.e(
                                "DeleteMyAddressesResponse",
                                "DeleteMyAddressesResponseSuccess->\n ${_stateDeleteMyAddress.value}"
                            )
                        } catch (e: Exception) {
                            Log.d("Exception", "${e.message} Exception")
                        }
                    }
                    is Resource.Error -> {
                        Log.e(
                            "DeleteMyAddressesResponse",
                            "DeleteMyAddressesResponseError->\n ${result.message}"
                        )
                        _stateUpdateMyAddress.value = UpdateMyAddressesResponseState(
                            error = "${result.message}"
                        )
                    }
                    is Resource.Loading -> {
                        _stateUpdateMyAddress.value =
                            UpdateMyAddressesResponseState(isLoading = true)
                    }
                }
            }.launchIn(viewModelScope)
    }

}