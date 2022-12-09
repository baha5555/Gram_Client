package com.example.gramclient.presentation.authorization

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.gramclient.PreferencesName
import com.example.gramclient.Resource
import com.example.gramclient.RoutesName
import com.example.gramclient.domain.athorization.AuthResponse
import com.example.gramclient.domain.athorization.AuthUseCase
import com.example.gramclient.domain.athorization.IdentificationResponse
import com.example.gramclient.domain.athorization.IdentificationUseCase
import com.example.gramclient.presentation.authorization.states.AuthResponseState
import com.example.gramclient.presentation.authorization.states.IdentificationResponseState
import com.example.gramclient.presentation.mainScreen.states.SearchAddressResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCase: AuthUseCase ,
    private val identificationUseCase: IdentificationUseCase
): ViewModel() {



     val _stateAuth = mutableStateOf(AuthResponseState())
    val stateAuth: State<AuthResponseState> = _stateAuth


    private val _stateLogin = mutableStateOf(IdentificationResponseState())
    val stateLogin: State<IdentificationResponseState> = _stateLogin



    fun authorization(phone: Int, preferences: SharedPreferences, navController: NavHostController){
        preferences.edit()
            .putString(PreferencesName.PHONE_NUMBER, phone.toString())
            .apply()
        authUseCase.invoke(phone).onEach { result: Resource<AuthResponse> ->
            when (result){
                is Resource.Success -> {
                    try {
                        val response: AuthResponse? = result.data
                        _stateAuth.value =
                            AuthResponseState(response = response)
                        Log.e("TariffsResponse", "AuthResponse->\n ${_stateAuth.value}")
                        if (response != null) {
                            preferences.edit()
                                .putString(PreferencesName.CLIENT_REGISTER_ID, response.result.client_register_id)
                                .apply()
                        }
                        navController.navigate(RoutesName.IDENTIFICATION_SCREEN) {
                            popUpTo(RoutesName.AUTH_SCREEN) {
                                inclusive = true
                            }
                        }
                    }catch (e: Exception) {
                        Log.d("Exception", "${e.message} Exception")
                    }
                }
                is Resource.Error -> {
                    Log.e("TariffsResponse", "AuthResponseError->\n ${result.message}")
                    _stateAuth.value = AuthResponseState(
                        error = "${result.message}"
                    )
                }
                is Resource.Loading -> {
                    _stateAuth.value = AuthResponseState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun identification(
        sms_code: String,
        preferences: SharedPreferences,
        navController: NavHostController
    ){
        var code=sms_code.toInt()
        identificationUseCase.invoke(preferences.getString(PreferencesName.CLIENT_REGISTER_ID, "").toString(), code).onEach { result: Resource<IdentificationResponse> ->
            when (result) {
                is Resource.Success -> {
                    val response = result.data
                    _stateLogin.value =
                        IdentificationResponseState(response = response?.result)
                    Log.e("authresponse", "authresponse->\n ${_stateLogin.value}")
                    preferences.edit()
                        .putString(PreferencesName.ACCESS_TOKEN, response?.result?.access_token)
                        .apply()
                    navController.navigate(RoutesName.SEARCH_ADDRESS_SCREEN) {
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