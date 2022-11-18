package com.example.gramclient.presentation

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.gramclient.PreferencesName
import com.example.gramclient.RoutesName
import com.example.gramclient.presentation.authorization.AuthViewModel
import com.example.gramclient.presentation.drawer_bar.messageScreen.MessageScreen
import com.example.gramclient.presentation.drawer_bar.messageScreen.MessageViewModel
import com.example.gramclient.presentation.drawer_bar.myaddresses_screen.AddAddressScreen
import com.example.gramclient.presentation.drawer_bar.myaddresses_screen.EditAddressScreen
import com.example.gramclient.presentation.drawer_bar.myaddresses_screen.MyAddressesScreen
import com.example.gramclient.presentation.drawer_bar.setting_screens.SettingLanguageScreen
import com.example.gramclient.presentation.drawer_bar.setting_screens.SettingRegionScreen
import com.example.gramclient.presentation.drawer_bar.setting_screens.SettingScreen
import com.example.gramclient.presentation.drawer_bar.setting_screens.SettingSelectRegionScreen

@Composable
fun Navigation(
    navController: NavHostController,
    messageViewModel: Lazy<MessageViewModel>,
    preferences: SharedPreferences,
    authViewModel: Lazy<AuthViewModel>
) {
    NavHost(
        navController = navController,
        startDestination = if (!preferences.getBoolean(
                PreferencesName.IS_AUTH,
                false
            )
        ) RoutesName.MAIN_SCREEN else RoutesName.MAIN_SCREEN
    ) {
        composable(RoutesName.SPLASH_SCREEN) {
            SplashScreen(navController)
        }
        composable(RoutesName.IDENTIFICATION_SCREEN) {
            IdentificationScreen(
                modifier = Modifier.fillMaxWidth(),
                onFilled = { Log.d("Tag", "Hello") },
                navController = navController,
                preferences = preferences,
                viewModel = authViewModel
            )
        }
        composable(RoutesName.AUTH_SCREEN) {
            AuthorizationScreen(navController, authViewModel)
        }
        composable(RoutesName.MAIN_SCREEN) {
            MainScreen(navController, preferences)
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
        composable(RoutesName.ORDEREXECUTION_SCREEN) {
            OrderExecution(navController)
        }
    }
}

