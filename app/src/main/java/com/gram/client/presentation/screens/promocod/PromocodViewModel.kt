package com.gram.client.presentation.screens.promocod

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gram.client.domain.promocod.*
import com.gram.client.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class PromocodViewModel @Inject constructor(
    private val getPromoCodeUseCase: GetPromocodUseCase,
    private val sendPromoCodeUseCase: SendPromoCodeUseCase
) : ViewModel() {
    private val _statepromocod = mutableStateOf(GetPromocodResponseState())
    val statepromocod: MutableState<GetPromocodResponseState> = _statepromocod

    private val _stateSendPromoCode = mutableStateOf(GetPromocodResponseState())
    val stateSendPromocod: State<GetPromocodResponseState> = _stateSendPromoCode


    fun getPromoCode() {
        getPromoCodeUseCase.invoke().onEach { result: Resource<PromoCode> ->
            when (result) {
                is Resource.Success -> {
                    try {
                        val promocodResponse: PromoCode? = result.data
                        _statepromocod.value =
                            GetPromocodResponseState(response = promocodResponse?.result)
                        Log.e(
                            "GetPromocodResponse",
                            "GetPromocodResponseSuccess->\n ${_statepromocod.value}"
                        )
                    } catch (e: Exception) {
                        Log.d("Exception", "${e.message} Exception")
                    }
                }
                is Resource.Error -> {
                    Log.e("GetPromocodResponse", "GetPromocodResponseError->\n ${result.message}")
                    _statepromocod.value = GetPromocodResponseState(
                        error = "${result.message}"
                    )
                }
                is Resource.Loading -> {
                    _statepromocod.value = GetPromocodResponseState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun sendPromoCode(promoCode: String) {
         sendPromoCodeUseCase.invoke(if(promoCode=="") null else promoCode).onEach { result: Resource<PromoCode> ->
            when (result) {
                is Resource.Success -> {
                    try {
                        val sendPromoCodeResponse: PromoCode? = result.data
                        _stateSendPromoCode.value =
                            GetPromocodResponseState(response = sendPromoCodeResponse?.result)
                        Log.e(
                            "sendPromocodResponse",
                            "sendPromocodResponseSuccess->\n ${_stateSendPromoCode.value}"
                        )
                    } catch (e: Exception) {
                        Log.d("sendPromocodResponse", "${e.message} Exception")
                    }
                }
                is Resource.Error -> {
                    Log.e("sendPromocodResponse", "sendPromocodResponseError->\n ${result.message}")
                    _stateSendPromoCode.value = GetPromocodResponseState(
                        error = "${result.message}"
                    )
                }
                is Resource.Loading -> {
                    _stateSendPromoCode.value = GetPromocodResponseState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}