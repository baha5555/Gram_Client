package com.example.gramclient.presentation

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.gramclient.PreferencesName
import com.example.gramclient.RoutesName
import com.example.gramclient.presentation.authorization.AuthorizationScreen
import com.example.gramclient.presentation.drawer_bar.messageScreen.MessageScreen
import com.example.gramclient.presentation.drawer_bar.messageScreen.MessageViewModel
import com.example.gramclient.presentation.drawer_bar.myaddresses_screen.AddAddressScreen
import com.example.gramclient.presentation.drawer_bar.myaddresses_screen.EditAddressScreen
import com.example.gramclient.presentation.drawer_bar.myaddresses_screen.MyAddressesScreen
import com.example.gramclient.presentation.drawer_bar.setting_screens.SettingLanguageScreen
import com.example.gramclient.presentation.drawer_bar.setting_screens.SettingRegionScreen
import com.example.gramclient.presentation.drawer_bar.setting_screens.SettingScreen
import com.example.gramclient.presentation.drawer_bar.setting_screens.SettingSelectRegionScreen
import com.example.gramclient.presentation.mainScreen.AddressSearchScreen
import com.example.gramclient.presentation.mainScreen.MainScreen
import com.example.gramclient.presentation.mainScreen.MainViewModel
import com.example.gramclient.presentation.mainScreen.SearchAddressScreen
import com.example.gramclient.presentation.orderScreen.OrderExecution

@Composable
fun Navigation(
    navController: NavHostController,
    messageViewModel: Lazy<MessageViewModel>,
    preferences: SharedPreferences,
    mainViewModel: MainViewModel= hiltViewModel()

    ) {
    NavHost(
        navController = navController,
        startDestination = if (preferences.getString(PreferencesName.ACCESS_TOKEN, "") == "")
            RoutesName.SPLASH_SCREEN else RoutesName.SEARCH_ADDRESS_SCREEN
    ) {
        composable(RoutesName.SPLASH_SCREEN) {
            SplashScreen(navController)
        }
        composable(RoutesName.IDENTIFICATION_SCREEN) {
            IdentificationScreen(
                modifier = Modifier.fillMaxWidth(),
                onFilled = { Log.d("Tag", "Hello") },
                navController = navController,
                preferences = preferences
            )
        }
        composable(RoutesName.AUTH_SCREEN) {
            AuthorizationScreen(navController, preferences=preferences)
        }
        composable(RoutesName.MAIN_SCREEN) {
            MainScreen(navController, preferences, mainViewModel)
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
            ProfileScreen(navController,preferences=preferences)
        }
        composable(RoutesName.MESSAGE_SCREEN) {
            MessageScreen(navController, messageViewModel)
        }
        composable(RoutesName.ABOUT_SCREEN) {
            AboutScreen(navController)
        }
        composable(RoutesName.ORDERS_HISTORY_SCREEN) {
            OrdersHistoryScreen(navController, preferences = preferences)
        }
        composable(RoutesName.PROMO_CODE_SCREEN) {
            PromoCodeScreen(navController)
        }
        composable(RoutesName.ORDER_EXECUTION_SCREEN) {
            OrderExecution(navController, preferences)
        }
        composable(RoutesName.SEARCH_DRIVER_SCREEN) {
            SearchDriverScreen(navController, preferences=preferences)
        }
        composable(
            "${RoutesName.SEARCH_ADDRESS_SCREEN}/{searchId}",
            arguments = listOf(navArgument("searchId"){type= NavType.StringType})
        ) {backStackEntry ->
            SearchAddressScreen(navController, preferences=preferences, string = backStackEntry.arguments?.getString("searchId"))
        }
        composable(RoutesName.SEARCH_ADDRESS_SCREEN) {
            AddressSearchScreen(navController, preferences, mainViewModel)
        }
    }
}

