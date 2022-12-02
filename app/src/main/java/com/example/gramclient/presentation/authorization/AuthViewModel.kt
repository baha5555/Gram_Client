package com.example.gramclient.presentation.authorization

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.gramclient.PreferencesName
import com.example.gramclient.Resource
import com.example.gramclient.RoutesName
import com.example.gramclient.data.AppRepositoryImpl
import com.example.gramclient.domain.athorization.AuthResponse
import com.example.gramclient.domain.athorization.AuthUseCase
import com.example.gramclient.domain.athorization.IdentificationResponse
import com.example.gramclient.domain.athorization.IdentificationUseCase
import com.example.gramclient.presentation.authorization.states.IdentificationResponseState
import com.example.gramclient.presentation.mainScreen.states.TariffsResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCase: AuthUseCase ,
    private val identificationUseCase: IdentificationUseCase
): ViewModel() {

    var phoneNumber = ""
    var client_register_id=""

    private val _stateLogin = mutableStateOf(IdentificationResponseState())
    val stateLogin: State<IdentificationResponseState> = _stateLogin

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

    fun identification(
        sms_code: List<Char>,
        preferences: SharedPreferences,
        navController: NavHostController
    ){
        var code=String(sms_code.toCharArray()).toInt()
        identificationUseCase.invoke(client_register_id, code).onEach { result: Resource<IdentificationResponse> ->
            when (result) {
                is Resource.Success -> {
                    val response = result.data
                    _stateLogin.value =
                        IdentificationResponseState(response = response?.result)
                    Log.e("authresponse", "authresponse->\n ${_stateLogin.value}")
                    preferences.edit()
                        .putString(PreferencesName.ACCESS_TOKEN, response?.result?.access_token)
                        .apply()
                    navController.navigate(RoutesName.MAIN_SCREEN) {
                        popUpTo(RoutesName.IDENTIFICATION_SCREEN) {
                            inclusive = true
                        }
                    }
                }
                is Resource.Error -> {
                    Log.e("authresponse", "authresponseError->\n ${result.message}")
                    _stateLogin.value = IdentificationResponseState(
                        error = "Введён неправильный код"
                    )
                }
                is Resource.Loading -> {
                    _stateLogin.value = IdentificationResponseState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}