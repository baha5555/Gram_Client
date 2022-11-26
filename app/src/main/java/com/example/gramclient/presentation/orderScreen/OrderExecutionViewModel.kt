package com.example.gramclient.presentation.orderScreen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gramclient.Resource
import com.example.gramclient.data.AppRepositoryImpl
import com.example.gramclient.domain.mainScreen.SearchAddressResponse
import com.example.gramclient.domain.orderExecutionScreen.AddRatingResponse
import com.example.gramclient.domain.orderExecutionScreen.AddRatingResponseState
import com.example.gramclient.domain.orderExecutionScreen.SendAddRatingUseCase
import com.example.gramclient.presentation.mainScreen.states.SearchAddressResponseState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class OrderExecutionViewModel: ViewModel() {
    private val repository= AppRepositoryImpl
    private val sendAddRatingUseCase: SendAddRatingUseCase = SendAddRatingUseCase(repository)

    private val _stateAddRating = mutableStateOf(AddRatingResponseState())
    val stateSearchAddress: State<AddRatingResponseState> = _stateAddRating
    fun sendRating2(token:String,
                      order_id: Int,
                      add_rating: Int){
        sendAddRatingUseCase.invoke(token="Bearer $token",order_id, add_rating).onEach { result: Resource<AddRatingResponse> ->
            when (result){
                is Resource.Success -> {
                    try {
                        val addressResponse: AddRatingResponse? = result.data
                        _stateAddRating.value =
                            AddRatingResponseState(response = addressResponse?.result)
                        Log.e("AddRatingResponse", "SendRatingResponse->\n ${_stateAddRating.value}")
                    }catch (e: Exception) {
                        Log.d("Exception", "${e.message} Exception")
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
}