package com.example.gramclient.presentation.orderScreen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gramclient.Resource
import com.example.gramclient.data.AppRepositoryImpl
import com.example.gramclient.domain.athorization.AuthResponse
import com.example.gramclient.domain.orderExecutionScreen.AddRatingResponse
import com.example.gramclient.domain.orderExecutionScreen.AddRatingResponseState
import com.example.gramclient.domain.orderExecutionScreen.SendAddRatingUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class OrderExecutionViewModel: ViewModel() {
    private val repository= AppRepositoryImpl
    private val sendAddRatingUseCase: SendAddRatingUseCase = SendAddRatingUseCase(repository)

    private val _addRating = mutableStateOf(AddRatingResponseState())
    val addRating: State<AddRatingResponseState> = _addRating

    fun sendRating(token:String,
                  order_id: Int,
                  add_rating: Int,
                    ){
        sendAddRatingUseCase.invoke(token="Bearer $token",order_id, add_rating).onEach { result: Resource<AddRatingResponse> ->
            when (result){
                is Resource.Success -> {
                    try {
                        val allowancesResponse: AddRatingResponse? = result.data
                        _addRating.value =
                            AddRatingResponseState(response = allowancesResponse?.result)
                        Log.e("AddRatingResponse", "AddRatingResponseError->\n ${_addRating.value}")
                    }catch (e: Exception) {
                        Log.d("Exception", "${e.message} Exception")
                    }
                }
                is Resource.Error -> {
                    Log.e("AddRatingResponse", "2AddRatingResponse->\n ${result.message}")
                    _addRating.value = AddRatingResponseState(
                        error = "${result.message}"
                    )
                }
                is Resource.Loading -> {
                    _addRating.value = AddRatingResponseState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}