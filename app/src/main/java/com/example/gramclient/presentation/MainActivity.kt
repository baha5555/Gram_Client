package com.example.gramclient.presentation

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.example.gramclient.presentation.messageScreen.MessageViewModel
import com.example.gramclient.ui.theme.GramClientTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        setContent {
            GramClientTheme {
                val messageViewModel= viewModels<MessageViewModel>()
                val navController= rememberNavController()
                Navigation(navController =navController, messageViewModel)
            }
        }
    }
}

suspend fun setInterval(interval: Long, action: () -> Unit) {
    while (true) {
        action()
        delay(interval)
    }
}