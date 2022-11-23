package com.example.gramclient.presentation.mainScreen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gramclient.PreferencesName
import com.example.gramclient.Resource
import com.example.gramclient.RoutesName
import com.example.gramclient.data.AppRepositoryImpl
import com.example.gramclient.domain.GetTariffsUseCase
import com.example.gramclient.domain.TariffsResponse
import com.example.gramclient.domain.TariffsResult
import com.example.gramclient.presentation.authorization.states.IdentificationResponseState
import com.example.gramclient.presentation.mainScreen.states.TariffsResponseState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainViewModel:ViewModel() {
    private val repository= AppRepositoryImpl
    private val getTariffsUseCase=GetTariffsUseCase(repository)


    private val _stateTariffs = mutableStateOf(TariffsResponseState())
    val stateTariffs: State<TariffsResponseState> = _stateTariffs

    fun getTariffs(token:String){
        getTariffsUseCase.invoke(token="Bearer $token").onEach { result: Resource<TariffsResponse> ->
            when (result){
                is Resource.Success -> {
                    try {
                        val tariffsResponse: TariffsResponse? = result.data
                        _stateTariffs.value =
                            TariffsResponseState(response = tariffsResponse?.result)
                        Log.e("TariffsResponse", "TariffsResponse->\n ${_stateTariffs.value}")
                    }catch (e: Exception) {
                        Log.d("Exception", "${e.message} Exception")
                    }
                }
                is Resource.Error -> {
                    Log.e("TariffsResponse", "TariffsResponseError->\n ${result.message}")
                    _stateTariffs.value = TariffsResponseState(
                        error = "${result.message}"
                    )
                }
                is Resource.Loading -> {
                    _stateTariffs.value = TariffsResponseState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}