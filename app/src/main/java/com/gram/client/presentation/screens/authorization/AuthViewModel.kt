package com.gram.client.presentation.screens.authorization

import android.app.Application
import android.content.Context
import android.os.Vibrator
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gram.client.app.preference.CustomPreference
import com.gram.client.domain.athorization.*
import com.gram.client.utils.Resource
import com.gram.client.presentation.screens.authorization.states.AuthResponseState
import com.gram.client.presentation.screens.authorization.states.IdentificationResponseState
import com.gram.client.utils.Values.CLIENT_REGISTER_ID
import com.gram.client.utils.Values.PHONE_NUMBER
import com.gram.client.utils.Values.SMS_CODE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    application: Application,
    private val authUseCase: AuthUseCase,
    private val identificationUseCase: IdentificationUseCase,
    private val prefs: CustomPreference
): AndroidViewModel(application) {
    val context get() = getApplication<Application>()
    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    private val _stateAuth = mutableStateOf(AuthResponseState())
    val stateAuth: State<AuthResponseState> = _stateAuth

    private val _stateLogin = mutableStateOf(IdentificationResponseState())
    val stateLogin: State<IdentificationResponseState> = _stateLogin



    private val _isAutoInsert = mutableStateOf(false)
    val isAutoInsert: State<Boolean> = _isAutoInsert

    fun updateIsAutoInsert(value:Boolean) {
        _isAutoInsert.value = value
    }

    fun setCodeAutomaticly(code:String, scope:CoroutineScope,fcm_token:String){
        scope.launch {
            SMS_CODE.value = code
            delay(2000)
            //identification(smsCode.value!!, client_regiter_id.value!!, navController,fcm_token)
        }
    }

    fun updateCode(code: String){
        SMS_CODE.value=code
    }

    fun authorization(phone: String, onSuccess: () -> Unit){
        PHONE_NUMBER.value=phone
        authUseCase.invoke(phone).onEach { result: Resource<AuthResponse> ->
            when (result){
                is Resource.Success -> {
                    try {
                        val response: AuthResponse? = result.data
                        _stateAuth.value = AuthResponseState(response = response)
                        Log.e("TariffsResponse", "AuthResponse->\n ${_stateAuth.value}")
                        CLIENT_REGISTER_ID.value= response?.result?.client_register_id.toString()
//                        Toast.makeText(context, ""+ (_stateAuth.value.response?.result?.sms_code ?: "not"), Toast.LENGTH_LONG).show()
                        onSuccess.invoke()
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
        identificationRequest: IdentificationRequest,
        onSuccess: () -> Unit
    ){
        identificationUseCase.invoke(identificationRequest).onEach { result: Resource<IdentificationResponse> ->
            when (result) {
                is Resource.Success -> {
                    val response = result.data
                    _stateLogin.value =
                        IdentificationResponseState(response = response?.result)
                    Log.e("authresponse", "authresponseSuccess->\n ${_stateLogin.value}\n")
                    prefs.setAccessToken("Bearer ${response?.result?.access_token}")
                    prefs.setSocketAccessToken("${response?.result?.access_token}")
                    SMS_CODE.value=""
                    onSuccess.invoke()
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