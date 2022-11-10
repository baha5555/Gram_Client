package com.example.gramclient.presentation

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.gramclient.RoutesName
import com.example.gramclient.presentation.messageScreen.MessageViewModel
import com.example.gramclient.presentation.myaddresses_screen.AddAddressScreen
import com.example.gramclient.presentation.myaddresses_screen.EditAddressScreen
import com.example.gramclient.presentation.myaddresses_screen.MyAddressesScreen
import com.example.gramclient.presentation.setting_screens.SettingLanguageScreen
import com.example.gramclient.presentation.setting_screens.SettingRegionScreen
import com.example.gramclient.presentation.setting_screens.SettingScreen
import com.example.gramclient.presentation.setting_screens.SettingSelectRegionScreen

@Composable
fun Navigation(navController: NavHostController, messageViewModel: Lazy<MessageViewModel>) {
    NavHost(
        navController = navController,
        startDestination = RoutesName.SPLASH_SCREEN
    ) {
        composable(RoutesName.SPLASH_SCREEN) {
            SplashScreen(navController)
        }
        composable(RoutesName.IDENTIFICATION_SCREEN) {
            IdentificationScreen(
                modifier = Modifier.fillMaxWidth(),
                onFilled = { Log.d("Tag", "Hello") },
                navController = navController
            )
        }
        composable(RoutesName.AUTH_SCREEN) {
            AuthorizationScreen(navController)
        }
        composable(RoutesName.MAIN_SCREEN) {
            MainScreen(navController)
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
    }
}

