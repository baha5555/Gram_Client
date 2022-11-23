package com.example.gramclient.presentation

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.example.gramclient.PreferencesName
import com.example.gramclient.presentation.authorization.AuthViewModel
import com.example.gramclient.presentation.drawer_bar.messageScreen.MessageViewModel
import com.example.gramclient.presentation.mainScreen.MainViewModel
import com.example.gramclient.ui.theme.GramClientTheme
import kotlinx.coroutines.delay

@Suppress("DEPRECATION")
class MainActivity : ComponentActivity() {
    private lateinit var preferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        setContent {
            GramClientTheme {
                val messageViewModel= viewModels<MessageViewModel>()
                val authViewModel= viewModels<AuthViewModel>()
                val mainViewModel= viewModels<MainViewModel>()
                val navController= rememberNavController()
                Navigation(navController =navController, messageViewModel, preferences, authViewModel, mainViewModel)
            }
        }
        preferences=getSharedPreferences(PreferencesName.APP_PREFERENCES, Context.MODE_PRIVATE)
    }
}

suspend fun setInterval(interval: Long, action: () -> Unit) {
    while (true) {
        action()
        delay(interval)
    }
}