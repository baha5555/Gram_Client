package com.example.gramclient.presentation.profile

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gramclient.Resource
import com.example.gramclient.data.AppRepositoryImpl
import com.example.gramclient.domain.profile.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import okhttp3.MultipartBody

class ProfileViewModel:ViewModel() {
    private val repository= AppRepositoryImpl
    private val sendProfileUseCase: SendProfileUseCase = SendProfileUseCase(repository)
    private val getProfileInfoUseCase:GetProfileInfoUseCase = GetProfileInfoUseCase(repository)
    private val _stateprofile = mutableStateOf(ProfileResponseState())
    val stateAllowances: State<ProfileResponseState> = _stateprofile

    private val _stateListOfGenders = mutableStateOf(listOf("Мужской", "Женский"))
    val stateListOfGenders: State<List<String>> = _stateListOfGenders

    private val _genderId = mutableStateOf(-1)
    val genderId: State<Int> = _genderId

    fun setGenderId(id: Int) {
        _genderId.value = id
    }

    private val _stateGetProfileInfo = mutableStateOf(GetProfileInfoResponseState())
    val stateGetProfileInfo: State<GetProfileInfoResponseState> = _stateGetProfileInfo

    fun getProfileInfo(token:String){
        getProfileInfoUseCase.invoke(token="Bearer $token").onEach { result: Resource<GetProfileInfoResponse> ->
            when (result){
                is Resource.Success -> {
                    try {
                        val tariffsResponse: GetProfileInfoResponse? = result.data
                        _stateGetProfileInfo.value =
                            GetProfileInfoResponseState(response = tariffsResponse?.result)
                        Log.e("TariffsResponse", "TariffsResponse->\n ${_stateGetProfileInfo.value}")
                    }catch (e: Exception) {
                        Log.d("Exception", "${e.message} Exception")
                    }
                }
                is Resource.Error -> {
                    Log.e("TariffsResponse", "TariffsResponseError->\n ${result.message}")
                    _stateGetProfileInfo.value = GetProfileInfoResponseState(
                        error = "${result.message}"
                    )
                }
                is Resource.Loading -> {
                    _stateGetProfileInfo.value = GetProfileInfoResponseState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }


    fun sendProfile(token:String,
                    first_name: String,
                    last_name: String,
                    gender: String,
                    birth_date: String,
                    email: String,
                    images: MultipartBody.Part
                    ){
        sendProfileUseCase.invoke(token="Bearer $token",first_name,last_name, gender, birth_date, email, avatar = images).onEach { result: Resource<ProfileResponse> ->
            when (result){
                is Resource.Success -> {
                    try {
                        val allowancesResponse: ProfileResponse? = result.data
                        _stateprofile.value =
                            ProfileResponseState(response = allowancesResponse?.result)
                        getProfileInfo(token)
                        Log.e("ProfileResponse", "SendProfileSuccess->\n ${_stateprofile.value}")
                    }catch (e: Exception) {
                        Log.d("Exception", "${e.message} Exception")
                    }
                }
                is Resource.Error -> {
                    Log.e("ProfileResponse", "SendProfileErorr->\n ${result.message}")
                    _stateprofile.value = ProfileResponseState(
                        error = "${result.message}"
                    )
                }
                is Resource.Loading -> {
                    _stateprofile.value = ProfileResponseState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}