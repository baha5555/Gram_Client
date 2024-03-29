package com.gram.client.presentation.screens.profile

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gram.client.utils.Resource
import com.gram.client.domain.profile.*
import com.gram.client.utils.Values
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val sendProfileUseCase: SendProfileUseCase,
    private val getProfileInfoUseCase: GetProfileInfoUseCase
) : ViewModel() {
    private val _stateprofile = mutableStateOf(ProfileResponseState())
    val stateprofile: State<ProfileResponseState> = _stateprofile

    private val _stateListOfGenders = mutableStateOf(listOf("Мужской", "Женский"))
    val stateListOfGenders: State<List<String>> = _stateListOfGenders

    private val _genderId = mutableStateOf(-1)
    val genderId: State<Int> = _genderId

    fun setGenderId(id: Int) {
        _genderId.value = id
    }

    private val _stateGetProfileInfo = mutableStateOf(GetProfileInfoResponseState())
    val stateGetProfileInfo: State<GetProfileInfoResponseState> = _stateGetProfileInfo

    fun getProfileInfo() {
        getProfileInfoUseCase.invoke().onEach { result: Resource<GetProfileInfoResponse> ->
            when (result) {
                is Resource.Success -> {
                    try {
                        val tariffsResponse: GetProfileInfoResponse? = result.data
                        _stateGetProfileInfo.value =
                            GetProfileInfoResponseState(
                                response = tariffsResponse?.result,
                                success = true
                            )
                        stateGetProfileInfo.value.response.let {
                            Values.FirstName.value = it?.first_name ?: ""
                            Values.LastName.value = it?.last_name ?: ""
                            Values.Email.value = it?.email ?: ""
                            Values.ImageUrl.value = it?.avatar ?: ""
                            Values.PhoneNumber.value = it?.phone ?: ""
                        }
                        Log.e(
                            "GetProfileResponse",
                            "GetProfileResponseSuccess->\n ${_stateGetProfileInfo.value}"
                        )
                    } catch (e: Exception) {
                        Log.d("Exception", "${e.message} Exception")
                    }
                }
                is Resource.Error -> {
                    Log.e("GetProfileResponse", "GetProfileResponseError->\n ${result.message}")
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


    fun sendProfile(
        sendProfileInfoRequest: ProfileInfoSendModel,
        onSuccess: () -> Unit = {},
    ) {

        sendProfileUseCase.invoke(sendProfileInfoRequest)
            .onEach { result: Resource<ProfileResponse> ->
                when (result) {
                    is Resource.Success -> {
                        try {
                            val allowancesResponse: ProfileResponse? = result.data
                            _stateprofile.value =
                                ProfileResponseState(response = allowancesResponse?.result?.get(0))
                            getProfileInfo()
                            onSuccess()
                            Log.e(
                                "ProfileResponse",
                                "SendProfileSuccess->\n ${_stateprofile.value}"
                            )
                        } catch (e: Exception) {
                            Log.d("Exception", "${e.message} Exception")
                        }
                    }
                    is Resource.Error -> {
                        Log.e("ProfileResponse", "SendProfileErorr->\n${result.message}\n ${result.data}")
                        _stateprofile.value = ProfileResponseState(
                            error = result.data?.error
                        )
                    }
                    is Resource.Loading -> {
                        _stateprofile.value = ProfileResponseState(isLoading = true)
                    }
                }
            }.launchIn(viewModelScope)
    }
    fun sendAvatar(
        avatar: MultipartBody.Part,
        onSuccess: () -> Unit = {},
    ) {

        sendProfileUseCase.sendAvatar(avatar)
            .onEach { result: Resource<ProfileResponse> ->
                when (result) {
                    is Resource.Success -> {
                        try {
                            val allowancesResponse: ProfileResponse? = result.data
                            _stateprofile.value =
                                ProfileResponseState(response = allowancesResponse?.result?.get(0))
                            getProfileInfo()
                            onSuccess()
                            Log.e(
                                "ProfileResponse",
                                "SendProfileSuccess->\n ${_stateprofile.value}"
                            )
                        } catch (e: Exception) {
                            Log.d("Exception", "${e.message} Exception")
                        }
                    }
                    is Resource.Error -> {
                        Log.e("ProfileResponse", "SendProfileErorr->\n${result.message}\n ${result.data}")
                        _stateprofile.value = ProfileResponseState(
                            error = result.data?.error
                        )
                    }
                    is Resource.Loading -> {
                        _stateprofile.value = ProfileResponseState(isLoading = true)
                    }
                }
            }.launchIn(viewModelScope)
    }
}