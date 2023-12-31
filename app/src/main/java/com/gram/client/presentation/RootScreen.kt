package com.gram.client.presentation

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import com.gram.client.app.preference.CustomPreference
import com.gram.client.presentation.components.CustomRequestError
import com.gram.client.presentation.screens.SplashScreen
import com.gram.client.presentation.screens.drawer.myaddresses_screen.MyAddressViewModel
import com.gram.client.presentation.screens.main.MainViewModel
import com.gram.client.presentation.screens.order.OrderExecutionViewModel
import com.gram.client.presentation.screens.profile.ProfileViewModel
import com.gram.client.utils.Constants
import org.burnoutcrew.reorderable.*

@SuppressLint("StaticFieldLeak")

@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RootScreen() {
    val orderExecutionViewModel: OrderExecutionViewModel = hiltViewModel()
    val context = LocalContext.current
    val prefs = CustomPreference(context)
    DisposableEffect(key1 = true){
        Log.i("TokenAccess", ""+prefs.getAccessToken())
        orderExecutionViewModel.getActiveOrders()
        onDispose {  }
    }
    Permissions()
    BottomSheetNavigator(sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)) {
        Box {
            Navigator(screen = SplashScreen())
//            Column {
//                Text(text = ""+ Values.currentRoute.value)
//                Text(text = ""+ Values.WhichAddress.value)
//                Text(text = ""+orderExecutionViewModel.stateActiveOrdersList.size)
//            }
        }
    }
    ReturnRequest()
    if (Constants.STATE_DRIVER_IN_SITE.value) {
//        orderExecutionViewModel.stateRealtimeOrdersDatabase.value.response?.let { response ->
//            response.observeAsState().value?.let {
//                for (i in it) {
//                    if (i.id == Constants.STATE_DRIVER_IN_SITE_ORDER_ID.value) DriverInSiteScreen(i, isDialog = Constants.STATE_DRIVER_IN_SITE)
//                }
//            }
//        }
    }
    //Text(text = ""+ (Values.ClientOrders.value?.active_orders?.size ?: -1)+"\nValue="+Values.ClientOrders.value)
}

@Composable
fun ReturnRequest(
    mainViewModel: MainViewModel = hiltViewModel(),
    orderExecutionViewModel: OrderExecutionViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel(),
    myAddressViewModel: MyAddressViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val prefs = CustomPreference(context)
    val activeOrders = orderExecutionViewModel.stateActiveOrders.value
    val profileInfo = profileViewModel.stateGetProfileInfo.value
    val fastAddresses = mainViewModel.stateFastAddress.value
    val myAddresses = myAddressViewModel.stateGetAllMyAddresses.value

    if (( profileInfo.error + fastAddresses.error) != "")
        CustomRequestError {
            if (profileInfo.error != "") profileViewModel.getProfileInfo()
            if (fastAddresses.error != "") mainViewModel.getFastAddresses()
            if(prefs.getAccessToken()!=""){
                if (myAddresses.error != "") myAddressViewModel.getAllMyAddresses()
            }
        } else if(prefs.getAccessToken()!="" && activeOrders.error != ""){
        if (activeOrders.error != "") orderExecutionViewModel.getActiveOrders()
    }
}