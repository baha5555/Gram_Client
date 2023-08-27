package com.gram.client.presentation

import android.app.AlertDialog
import android.content.*
import android.content.res.Configuration
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.gram.client.*
import com.gram.client.ui.theme.GramClientTheme
import com.gram.client.utils.MyFirebaseMessagingService
import com.gram.client.app.preference.CustomPreference
import com.gram.client.presentation.screens.order.OrderExecutionViewModel
import dagger.hilt.android.AndroidEntryPoint


@Suppress("DEPRECATION")
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        //window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        setContent {
            val orderExecutionViewModel: OrderExecutionViewModel = hiltViewModel()
            GramClientTheme {
                RootScreen()
                MyFirebaseMessagingService().onCreate()
            }
            Connect(orderExecutionViewModel)
        }
        adjustFontScale(getResources().getConfiguration())

    }

    fun Connect(orderExecutionViewModel: OrderExecutionViewModel) {
        val customPreference = CustomPreference(this).getSocketAccessToken()
        Log.i("tokenSocket", customPreference)
        SocketHandler.setSocket(
            customPreference,
            orderExecutionViewModel
        )
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
    private fun checkGPS(locationManager: LocationManager){
        var dialog: AlertDialog? = null
        if (!(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER))) {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Для продолжения работы приложения необходимо включить GPS-приемник")
                .setPositiveButton("Настройки") { _, _ ->
                    val locationIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(locationIntent)
                }
                .setCancelable(true)
            dialog = builder.create()
            dialog?.setCanceledOnTouchOutside(false)
            dialog?.setOnKeyListener { dialog, keyCode, event -> true }
            dialog?.show()
        }
    }
}