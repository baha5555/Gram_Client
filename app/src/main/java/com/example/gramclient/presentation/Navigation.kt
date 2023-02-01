package com.example.gramclient.presentation

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.gramclient.presentation.screens.SplashScreen
import com.example.gramclient.utils.RoutesName
import com.example.gramclient.presentation.screens.authorization.AuthViewModel
import com.example.gramclient.presentation.screens.authorization.AuthorizationScreen
import com.example.gramclient.presentation.screens.authorization.IdentificationScreen
import com.example.gramclient.presentation.screens.drawer.messageScreen.MessageScreen
import com.example.gramclient.presentation.screens.drawer.messageScreen.MessageViewModel
import com.example.gramclient.presentation.screens.drawer.myaddresses_screen.AddAddressScreen
import com.example.gramclient.presentation.screens.drawer.myaddresses_screen.EditAddressScreen
import com.example.gramclient.presentation.screens.drawer.myaddresses_screen.MyAddressesScreen
import com.example.gramclient.presentation.screens.drawer.setting_screens.SettingLanguageScreen
import com.example.gramclient.presentation.screens.drawer.setting_screens.SettingRegionScreen
import com.example.gramclient.presentation.screens.drawer.setting_screens.SettingScreen
import com.example.gramclient.presentation.screens.drawer.setting_screens.SettingSelectRegionScreen
import com.example.gramclient.presentation.screens.drawer.supportScreen.SupportScreen
import com.example.gramclient.presentation.screens.main.AddressSearchScreen
import com.example.gramclient.presentation.screens.main.MainScreen
import com.example.gramclient.presentation.screens.main.MainViewModel
import com.example.gramclient.presentation.screens.order.OrderExecution
import com.example.gramclient.presentation.screens.order.OrderExecutionViewModel
import com.example.gramclient.presentation.screens.order.SearchDriverScreen
import com.example.gramclient.presentation.screens.profile.ProfileScreen
import com.example.gramclient.utils.Constants
import com.example.gramclient.utils.Constants.STATE_DRIVER_IN_SITE
import com.example.gramclient.utils.Constants.STATE_DRIVER_IN_SITE_ORDER_ID


@SuppressLint("SuspiciousIndentation")
@Composable
fun Navigation(
    navController: NavHostController,
    messageViewModel: Lazy<MessageViewModel>,
    mainViewModel: MainViewModel = hiltViewModel(),
    authViewModel: AuthViewModel,
    orderExecutionViewModel: OrderExecutionViewModel = hiltViewModel()

) {
    var pressedTime: Long = 0
    val activity = (LocalContext.current as? MainActivity)
    val context= LocalContext.current

    if(Constants.STATE_DRIVER_IN_SITE.value)
    {
        orderExecutionViewModel.stateRealtimeOrdersDatabase.value.response?.let { response ->
            response.observeAsState().value?.let {
                for (i in it)
                {
                    if(i.id == STATE_DRIVER_IN_SITE_ORDER_ID.value)
                    DriverInSiteScreen(i, isDialog = STATE_DRIVER_IN_SITE)
                }
            }
        }
    }
    else
    NavHost(
        navController = navController,
        startDestination = RoutesName.SPLASH_SCREEN
    ) {
        composable(RoutesName.SPLASH_SCREEN) {
            SplashScreen(navController, orderExecutionViewModel)
        }
        composable(RoutesName.IDENTIFICATION_SCREEN) {
            IdentificationScreen(
                modifier = Modifier.fillMaxWidth(),
                navController = navController,
                viewModel = authViewModel
            )

        }
        composable(RoutesName.AUTH_SCREEN) {
            AuthorizationScreen(navController, viewModel = authViewModel)
        }
        composable(RoutesName.MAIN_SCREEN) {
            MainScreen(navController, mainViewModel, orderExecutionViewModel)
        }
        composable(RoutesName.SETTING_SCREEN) {
            SettingScreen(navController)
        }
        composable(RoutesName.SETTING_LANGUAGE_SCREEN) {
            SettingLanguageScreen(navController)
        }
        composable(RoutesName.SETTING_REGION_SCREEN) {
            SettingRegionScreen(navController)
        }
        composable(RoutesName.SETTING_SELECT_REGION_SCREEN) {
            SettingSelectRegionScreen(navController)
        }
        composable(RoutesName.MY_ADDRESSES_SCREEN) {
            MyAddressesScreen(navController)
        }
        composable(RoutesName.ADD_ADDRESSES_SCREEN) {
            AddAddressScreen(navController)
        }
        composable(RoutesName.EDIT_ADDRESSES_SCREEN) {
            EditAddressScreen(navController)
        }
        composable(RoutesName.SUPPORT_SCREEN) {
            SupportScreen(navController)
        }
        composable(RoutesName.PROFILE_SCREEN) {
            ProfileScreen(navController)
        }
        composable(RoutesName.MESSAGE_SCREEN) {
            MessageScreen(navController, messageViewModel)
        }
        composable(RoutesName.ABOUT_SCREEN) {
            AboutScreen(navController)
        }
        composable(RoutesName.ORDERS_HISTORY_SCREEN) {
            OrdersHistoryScreen(navController)
        }
        composable(RoutesName.PROMO_CODE_SCREEN) {
            PromoCodeScreen(navController)
        }
        composable(RoutesName.ORDER_EXECUTION_SCREEN) {
            OrderExecution(navController, orderExecutionViewModel)
        }
        composable(RoutesName.SEARCH_DRIVER_SCREEN) {
            SearchDriverScreen(navController, orderExecutionViewModel=orderExecutionViewModel)

        }
        composable(RoutesName.SEARCH_ADDRESS_SCREEN) {
            AddressSearchScreen(navController, mainViewModel)

        }
    }
}

