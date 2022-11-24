package com.example.gramclient.presentation.mainScreen

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gramclient.PreferencesName
import com.example.gramclient.Resource
import com.example.gramclient.data.AppRepositoryImpl
import com.example.gramclient.domain.mainScreen.*
import com.example.gramclient.presentation.mainScreen.states.AddressByPointResponseState
import com.example.gramclient.presentation.mainScreen.states.AllowancesResponseState
import com.example.gramclient.presentation.mainScreen.states.TariffsResponseState
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainViewModel:ViewModel() {
    private val repository= AppRepositoryImpl
    private val getTariffsUseCase= GetTariffsUseCase(repository)
    private val getAllowancesUseCase= GetAllowancesUseCase(repository)
    private val getAddressByPointUseCase= GetAddressByPointUseCase(repository)


    private val _stateTariffs = mutableStateOf(TariffsResponseState())
    val stateTariffs: State<TariffsResponseState> = _stateTariffs

    private val _stateAllowances = mutableStateOf(AllowancesResponseState())
    val stateAllowances: State<AllowancesResponseState> = _stateAllowances

    private val _stateAddressPoint = mutableStateOf(AddressByPointResponseState())
    val stateAddressPoint: State<AddressByPointResponseState> = _stateAddressPoint

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

    fun getAllowancesByTariffId(token:String, tariff_id:Int){
        getAllowancesUseCase.invoke(token="Bearer $token", tariff_id = tariff_id).onEach { result: Resource<AllowancesResponse> ->
            when (result){
                is Resource.Success -> {
                    try {
                        val allowancesResponse: AllowancesResponse? = result.data
                        _stateAllowances.value =
                            AllowancesResponseState(response = allowancesResponse?.result)
                        Log.e("TariffsResponse", "AllowancesResponseError->\n ${_stateAllowances.value}")
                    }catch (e: Exception) {
                        Log.d("Exception", "${e.message} Exception")
                    }
                }
                is Resource.Error -> {
                    Log.e("AllowancesResponse", "AllowancesResponse->\n ${result.message}")
                    _stateAllowances.value = AllowancesResponseState(
                        error = "${result.message}"
                    )
                }
                is Resource.Loading -> {
                    _stateAllowances.value = AllowancesResponseState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getAddressByPoint(token: String, lng: Double, lat: Double){
        getAddressByPointUseCase.invoke(token="Bearer $token", lng, lat).onEach { result: Resource<AddressByPointResponse> ->
            when (result){
                is Resource.Success -> {
                    try {
                        val addressResponse: AddressByPointResponse? = result.data
                        _stateAddressPoint.value =
                            AddressByPointResponseState(response = addressResponse?.result)
                        Log.e("TariffsResponse", "AllowancesResponseError->\n ${_stateAddressPoint.value}")
                    }catch (e: Exception) {
                        Log.d("Exception", "${e.message} Exception")
                    }
                }
                is Resource.Error -> {
                    Log.e("AllowancesResponse", "AllowancesResponse->\n ${result.message}")
                    _stateAddressPoint.value = AddressByPointResponseState(
                        error = "${result.message}"
                    )
                }
                is Resource.Loading -> {
                    _stateAddressPoint.value = AddressByPointResponseState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
    fun getActualLocation(context: Context, token: String) {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
            context)
        val task = fusedLocationProviderClient.lastLocation

        if (ActivityCompat
                .checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(context as Activity, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 101)
            return
        }

        task.addOnSuccessListener {
            if (it != null){
                getAddressByPoint(token, it.longitude, it.latitude)
                Log.e("TariffsResponse","Location - > ${it.longitude}  + ${it.latitude}")
            }
            else{
                Log.e("NULL", "NULL")
            }
        }

    }
}