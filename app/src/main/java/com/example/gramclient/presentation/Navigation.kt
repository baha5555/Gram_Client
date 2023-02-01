package com.example.gramclient.presentation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext


@SuppressLint("SuspiciousIndentation")
@Composable
fun Navigation() {
    var pressedTime: Long = 0
    val activity = (LocalContext.current as? MainActivity)
    val context= LocalContext.current
    /*if(Constants.STATE_DRIVER_IN_SITE.value)
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
    }*/

//    NavHost(
//        navController = navController,
//        startDestination = RoutesName.SPLASH_SCREEN
//    ) {
//        composable(RoutesName.SPLASH_SCREEN) {
//            //SplashScreen(navController, orderExecutionViewModel)
//        }
//        composable(RoutesName.IDENTIFICATION_SCREEN) {
////            IdentificationScreen(
////                modifier = Modifier.fillMaxWidth(),
////                navController = navController,
////                viewModel = authViewModel
////            )
//
//        }
//        composable(RoutesName.AUTH_SCREEN) {
//            //AuthorizationScreen(navController, viewModel = authViewModel)
//        }
//        composable(RoutesName.MAIN_SCREEN) {
//            //MainScreen(navController, mainViewModel, orderExecutionViewModel)
//        }
//        composable(RoutesName.SETTING_SCREEN) {
//            SettingScreen(navController)
//        }
//        composable(RoutesName.SETTING_LANGUAGE_SCREEN) {
//            SettingLanguageScreen(navController)
//        }
//        composable(RoutesName.SETTING_REGION_SCREEN) {
//            SettingRegionScreen(navController)
//        }
//        composable(RoutesName.SETTING_SELECT_REGION_SCREEN) {
//            SettingSelectRegionScreen(navController)
//        }
//        composable(RoutesName.MY_ADDRESSES_SCREEN) {
//            MyAddressesScreen(navController)
//        }
//        composable(RoutesName.ADD_ADDRESSES_SCREEN) {
//            AddAddressScreen(navController)
//        }
//        composable(RoutesName.EDIT_ADDRESSES_SCREEN) {
//            EditAddressScreen(navController)
//        }
//        composable(RoutesName.SUPPORT_SCREEN) {
//            SupportScreen(navController)
//        }
//        composable(RoutesName.PROFILE_SCREEN) {
//            //ProfileScreen(navController)
//        }
//        composable(RoutesName.MESSAGE_SCREEN) {
//            MessageScreen(navController, messageViewModel)
//        }
//        composable(RoutesName.ABOUT_SCREEN) {
//            //AboutScreen(navController)
//        }
//        composable(RoutesName.ORDERS_HISTORY_SCREEN) {
//            //OrdersHistoryScreen(navController)
//        }
//        composable(RoutesName.PROMO_CODE_SCREEN) {
//            PromoCodeScreen(navController)
//        }
//        composable(RoutesName.ORDER_EXECUTION_SCREEN) {
//            //OrderExecution(navController, orderExecutionViewModel)
//        }
//        composable(RoutesName.SEARCH_DRIVER_SCREEN) {
//            //SearchDriverScreen(navController, orderExecutionViewModel=orderExecutionViewModel)
//            BackHandler(enabled = true) {
//                if (pressedTime + 2000 > System.currentTimeMillis()) {
//                    activity?.finish()
//                } else {
//                    Toast.makeText(context, "Нажмите еще раз, чтобы выйти", Toast.LENGTH_SHORT).show();
//                }
//                pressedTime = System.currentTimeMillis();
//            }
//        }
//        composable(RoutesName.SEARCH_ADDRESS_SCREEN) {
//            //AddressSearchScreen(navController, mainViewModel)
//            BackHandler(enabled = true) {
//                if (pressedTime + 2000 > System.currentTimeMillis()) {
//                    activity?.finish()
//                } else {
//                    Toast.makeText(context, "Нажмите еще раз, чтобы выйти", Toast.LENGTH_SHORT).show();
//                }
//                pressedTime = System.currentTimeMillis();
//            }
//        }
//    }
}

