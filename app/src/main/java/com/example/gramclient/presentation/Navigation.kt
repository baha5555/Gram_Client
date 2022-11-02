package com.example.gramclient.presentation

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.gramclient.RoutesName

@Composable
fun Navigation (navController:NavHostController){
    NavHost(
        navController = navController,
        startDestination = RoutesName.SUBMITORDER_SCREEN
    ){
        composable(RoutesName.SPLASH_SCREEN){
            SplashScreen(navController)
        }
        composable(RoutesName.IDENTIFICATION_SCREEN){
            IdentificationScreen(modifier = Modifier.fillMaxWidth(), onFilled = { Log.d("Tag", "Hello")}, navController=navController)
        }
        composable(RoutesName.AUTH_SCREEN){
            AuthorizationScreen(navController)
        }
        composable(RoutesName.MAIN_SCREEN){
            MainScreen(navController)
        }
        composable(RoutesName.SUBMITORDER_SCREEN){
            SubmitOrderScreen(navController)
        }
    }
}

