package com.example.gramclient.presentation.profile

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gramclient.Resource
import com.example.gramclient.data.AppRepositoryImpl
import com.example.gramclient.domain.profile.ProfileResponse
import com.example.gramclient.domain.profile.ProfileResponseState
import com.example.gramclient.domain.profile.SendProfileUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.*

class ProfileViewModel:ViewModel() {
    private val repository= AppRepositoryImpl
    private val sendProfileUseCase: SendProfileUseCase = SendProfileUseCase(repository)

    private val _stateAllowances = mutableStateOf(ProfileResponseState())
    val stateAllowances: State<ProfileResponseState> = _stateAllowances

    fun sendProfile(token:String,
                                first_name: String,
                                last_name: String,
                                gender: String,
                                birth_date: Date,
                                email: String){
        sendProfileUseCase.invoke(token="Bearer $token",first_name,last_name, gender, birth_date, email).onEach { result: Resource<ProfileResponse> ->
            when (result){
                is Resource.Success -> {
                    try {
                        val allowancesResponse: ProfileResponse? = result.data
                        _stateAllowances.value =
                            ProfileResponseState(response = allowancesResponse?.result)
                        Log.e("TariffsResponse", "AllowancesResponseError->\n ${_stateAllowances.value}")
                    }catch (e: Exception) {
                        Log.d("Exception", "${e.message} Exception")
                    }
                }
                is Resource.Error -> {
                    Log.e("AllowancesResponse", "AllowancesResponse->\n ${result.message}")
                    _stateAllowances.value = ProfileResponseState(
                        error = "${result.message}"
                    )
                }
                is Resource.Loading -> {
                    _stateAllowances.value = ProfileResponseState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}