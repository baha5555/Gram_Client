package com.example.gramclient.presentation

import android.content.*
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import cafe.adriel.voyager.navigator.Navigator
import com.example.gramclient.*
import com.example.gramclient.presentation.screens.SplashScreen
import com.example.gramclient.presentation.screens.authorization.AuthViewModel
import com.example.gramclient.presentation.screens.order.OrderExecutionViewModel
import com.example.gramclient.ui.theme.GramClientTheme
import com.example.gramclient.utils.Constants
import com.example.gramclient.utils.Constants.FCM_TOKEN
import com.example.gramclient.utils.Constants.STATE_DRIVER_IN_SITE
import com.example.gramclient.utils.Constants.STATE_DRIVER_IN_SITE_ORDER_ID
import com.example.gramclient.utils.MyFirebaseMessagingService
import com.example.gramclient.utils.SmsBroadcastReceiver
import com.google.android.gms.auth.api.phone.SmsRetriever
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope


@Suppress("DEPRECATION")
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var authViewModel: AuthViewModel
    private lateinit var navController: NavHostController
    private lateinit var scope: CoroutineScope

    private val REQ_USER_CONSENT = 200
    var smsBroadcastReceiver: SmsBroadcastReceiver? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        setContent {
            GramClientTheme {
                val orderExecutionViewModel: OrderExecutionViewModel = hiltViewModel()
                navController = rememberNavController()
                scope = rememberCoroutineScope()
                Permissions()
                Navigator(screen = SplashScreen())
                if (STATE_DRIVER_IN_SITE.value) {
                    orderExecutionViewModel.stateRealtimeOrdersDatabase.value.response?.let { response ->
                        response.observeAsState().value?.let {
                            for (i in it) {
                                if (i.id == STATE_DRIVER_IN_SITE_ORDER_ID.value)
                                    DriverInSiteScreen(i, isDialog = STATE_DRIVER_IN_SITE)
                            }
                        }
                    }
                }
                MyFirebaseMessagingService().onCreate()
            }
        }
        //startSmartUserConsent()
    }

    private fun startSmartUserConsent() {
        val client = SmsRetriever.getClient(this)
        client.startSmsUserConsent(null)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_USER_CONSENT) {
            if (resultCode == RESULT_OK && data != null) {
                val message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
                if (message != null) {
                    authViewModel.updateIsAutoInsert(true)
                    getOtpFromMessage(message, authViewModel = authViewModel)
                }
            } else {
                authViewModel.updateIsAutoInsert(false)
            }
        }
    }

    private fun getOtpFromMessage(message: String, authViewModel: AuthViewModel) {
        var code = message.filter { it.isDigit() }
        Log.e("setEditValue", code)
        FCM_TOKEN?.let {
            authViewModel.setCodeAutomaticly(code, navController, scope, it)
        }
    }

    private fun registerBroadcastReceiver() {
        smsBroadcastReceiver = SmsBroadcastReceiver()
        smsBroadcastReceiver!!.smsBroadcastReceiverListener =
            object : SmsBroadcastReceiver.SmsBroadcastReceiverListener {
                override fun onSuccess(intent: Intent?) {
                    if (intent != null) {
                        startActivityForResult(intent, REQ_USER_CONSENT)
                    }
                }

                override fun onFailure() {}
            }
        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        registerReceiver(smsBroadcastReceiver, intentFilter)
    }

    override fun onStart() {
        super.onStart()
        registerBroadcastReceiver()
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(smsBroadcastReceiver)
    }
}