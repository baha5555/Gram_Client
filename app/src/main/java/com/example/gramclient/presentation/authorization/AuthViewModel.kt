package com.example.gramclient.presentation.authorization

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gramclient.Resource
import com.example.gramclient.data.AppRepositoryImpl
import com.example.gramclient.domain.athorization.AuthResponse
import com.example.gramclient.domain.athorization.AuthUseCase
import com.example.gramclient.domain.athorization.IdentificationResponse
import com.example.gramclient.domain.athorization.IdentificationUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class AuthViewModel: ViewModel() {
    private val repository=AppRepositoryImpl
    private val authUseCase= AuthUseCase(repository)
    private val identificationUseCase= IdentificationUseCase(repository)

    var phoneNumber = ""
    var client_register_id=""
    var accsess_token=""


    fun authorization(phone: Int){
        phoneNumber=phone.toString()
        authUseCase.invoke(phone).onEach { result: Resource<AuthResponse> ->
            when (result) {
                is Resource.Success -> {
                    val res = result.data
                    client_register_id= res?.result?.client_register_id ?: ""
                    Log.e("authresponse", "authresponse->\n ${res}")
                }
                is Resource.Error -> {
                    Log.e("authresponse", "authresponse->\n ${result.message}")

                }
                is Resource.Loading -> {
//                    _stateDriverInfo.value =
//                        DriverInfoState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun identification(sms_code: List<Char>){
        var code=String(sms_code.toCharArray()).toInt()
        identificationUseCase.invoke(client_register_id, code).onEach { result: Resource<IdentificationResponse> ->
            when (result) {
                is Resource.Success -> {
                    val res = result.data
                    accsess_token= res?.result?.access_token ?: ""
                    Log.e("authresponse", "authresponse->\n ${res}")
                }
                is Resource.Error -> {
                    Log.e("authresponse", "authresponse->\n ${result.message}")

                }
                is Resource.Loading -> {
//                    _stateDriverInfo.value =
//                        DriverInfoState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}