package com.gram.client.presentation.screens.promocod

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gram.client.domain.mainScreen.TariffsResponse
import com.gram.client.domain.profile.*
import com.gram.client.domain.promocod.GetPromocodResponse
import com.gram.client.domain.promocod.GetPromocodResponseState
import com.gram.client.domain.promocod.GetPromocodUseCase
import com.gram.client.domain.promocod.Promocod
import com.gram.client.presentation.screens.main.states.TariffsResponseState
import com.gram.client.utils.Resource
import com.gram.client.utils.Values
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class PromocodViewModel @Inject constructor(
    private val getPromocodUseCase: GetPromocodUseCase
) : ViewModel() {
    private val _statepromocod = mutableStateOf(GetPromocodResponseState())
    val statepromocod: MutableState<GetPromocodResponseState> = _statepromocod


    fun getPromocod() {
        getPromocodUseCase.invoke().onEach { result: Resource<GetPromocodResponse> ->
            when (result) {
                is Resource.Success -> {
                    try {
                        val promocodResponse: GetPromocodResponse? = result.data
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
}