package com.gram.client.presentation

import android.content.*
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import com.gram.client.*
import com.gram.client.presentation.screens.authorization.AuthViewModel
import com.gram.client.ui.theme.GramClientTheme
import com.gram.client.utils.Constants.FCM_TOKEN
import com.gram.client.utils.MyFirebaseMessagingService
import com.google.android.gms.auth.api.phone.SmsRetriever
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope


@Suppress("DEPRECATION")
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        //window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        setContent {
            GramClientTheme {
                RootScreen()
                MyFirebaseMessagingService().onCreate()
            }
        }
        adjustFontScale(getResources().getConfiguration())

    }

    fun adjustFontScale(configuration: Configuration) {
        if (configuration.fontScale > 1.10) {
            Log.e("TAGFONT", "fontScale=" + configuration.fontScale) //Custom Log class, you can use Log.w
            Log.e("TAGFONT", "font too big. scale down...") //Custom Log class, you can use Log.w
            configuration.fontScale = 1f
            val metrics = resources.displayMetrics
            val wm = getSystemService(WINDOW_SERVICE) as WindowManager
            wm.defaultDisplay.getMetrics(metrics)
            metrics.scaledDensity = configuration.fontScale * metrics.density
            baseContext.resources.updateConfiguration(configuration, metrics)
        }
    }
}