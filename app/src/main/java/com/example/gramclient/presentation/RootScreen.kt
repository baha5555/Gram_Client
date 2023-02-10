package com.example.gramclient.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.navigator.Navigator
import com.example.gramclient.presentation.components.CustomRequestError
import com.example.gramclient.presentation.screens.SplashScreen
import com.example.gramclient.presentation.screens.main.MainViewModel
import com.example.gramclient.presentation.screens.order.OrderExecutionViewModel
import com.example.gramclient.presentation.screens.profile.ProfileViewModel
import com.example.gramclient.utils.Constants

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RootScreen() {
    val orderExecutionViewModel: OrderExecutionViewModel = hiltViewModel()
    Permissions()
    Navigator(screen = SplashScreen())
    ReturnRequest()
    if (Constants.STATE_DRIVER_IN_SITE.value) {
        orderExecutionViewModel.stateRealtimeOrdersDatabase.value.response?.let { response ->
            response.observeAsState().value?.let {
                for (i in it) {
                    if (i.id == Constants.STATE_DRIVER_IN_SITE_ORDER_ID.value)
                        DriverInSiteScreen(i, isDialog = Constants.STATE_DRIVER_IN_SITE)
                }
            }
        }
    }
}

@Composable
fun ReturnRequest(
    mainViewModel: MainViewModel = hiltViewModel(),
    orderExecutionViewModel: OrderExecutionViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val activeOrders = orderExecutionViewModel.stateActiveOrders.value
    val profileInfo = profileViewModel.stateGetProfileInfo.value
    val fastAddresses = mainViewModel.stateFastAddress.value

    if ((activeOrders.error + profileInfo.error + fastAddresses.error) != "")
        CustomRequestError {
            if (activeOrders.error != "") orderExecutionViewModel.getActiveOrders()
            if (profileInfo.error != "") profileViewModel.getProfileInfo()
            if (fastAddresses.error != "") mainViewModel.getFastAddresses()
        }
}