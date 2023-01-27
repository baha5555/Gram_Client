package com.example.gramclient.presentation.screens.authorization

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.gramclient.app.preference.CustomPreference
import com.example.gramclient.utils.Resource
import com.example.gramclient.utils.RoutesName
import com.example.gramclient.domain.athorization.AuthResponse
import com.example.gramclient.domain.athorization.AuthUseCase
import com.example.gramclient.domain.athorization.IdentificationResponse
import com.example.gramclient.domain.athorization.IdentificationUseCase
import com.example.gramclient.presentation.screens.authorization.states.AuthResponseState
import com.example.gramclient.presentation.screens.authorization.states.IdentificationResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCase: AuthUseCase ,
    private val identificationUseCase: IdentificationUseCase,
    private val prefs: CustomPreference
): ViewModel() {



    private val _stateAuth = mutableStateOf(AuthResponseState())
    val stateAuth: State<AuthResponseState> = _stateAuth

    private val _stateLogin = mutableStateOf(IdentificationResponseState())
    val stateLogin: State<IdentificationResponseState> = _stateLogin

    val smsCode=MutableLiveData("")
    val client_regiter_id=MutableLiveData("")
    val phoneNumber=MutableLiveData("")

    private val _isAutoInsert = mutableStateOf(false)
    val isAutoInsert: State<Boolean> = _isAutoInsert

    fun updateIsAutoInsert(value:Boolean) {
        _isAutoInsert.value = value
    }

    fun setCodeAutomaticly(code:String, navController: NavHostController, scope:CoroutineScope,fcm_token:String){
        scope.launch {
            smsCode.value = code
            delay(2000)
            identification(smsCode.value!!, client_regiter_id.value!!, navController,fcm_token)
        }
    }

    fun updateCode(code: String){
        smsCode.value=code
    }

    fun authorization(phone: Int, navController: NavHostController){
        phoneNumber.value=phone.toString()
        authUseCase.invoke(phone).onEach { result: Resource<AuthResponse> ->
            when (result){
                is Resource.Success -> {
                    try {
                        val response: AuthResponse? = result.data
                        _stateAuth.value =
                            AuthResponseState(response = response)
                        Log.e("TariffsResponse", "AuthResponse->\n ${_stateAuth.value}")
                        client_regiter_id.value=response?.result?.client_register_id
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
        client_regiter_id:String,
        navController: NavHostController,
        fcm_token:String
    ){
        var code=sms_code
        identificationUseCase.invoke(client_regiter_id, code,fcm_token).onEach { result: Resource<IdentificationResponse> ->
            when (result) {
                is Resource.Success -> {
                    val response = result.data
                    _stateLogin.value =
                        IdentificationResponseState(response = response?.result)
                    Log.e("authresponse", "authresponseSuccess->\n ${_stateLogin.value}\n")
                    prefs.setAccessToken("Bearer ${response?.result?.access_token}")
                    navController.navigate(RoutesName.MAIN_SCREEN) {
                        popUpTo(RoutesName.IDENTIFICATION_SCREEN) {
                            inclusive = true
                        }
                    }
                    smsCode.value=""
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