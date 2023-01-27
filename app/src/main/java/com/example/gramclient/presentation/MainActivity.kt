package com.example.gramclient.presentation

import android.Manifest
import android.app.AlertDialog
import android.content.*
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.gramclient.*
import com.example.gramclient.utils.Constants.FCM_TOKEN
import com.example.gramclient.R
import com.example.gramclient.app.extension.checkInternet.ConnectionState
import com.example.gramclient.app.extension.checkInternet.connectivityState
import com.example.gramclient.presentation.screens.authorization.AuthViewModel
import com.example.gramclient.presentation.screens.drawer.messageScreen.MessageViewModel
import com.example.gramclient.ui.theme.GramClientTheme
import com.example.gramclient.utils.MyFirebaseMessagingService
import com.example.gramclient.utils.PreferencesName
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

    val FINE_LOCATION_RQ = 101
    val CAMERA_RQ = 102

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        setContent {
            GramClientTheme {
                val messageViewModel= viewModels<MessageViewModel>()
                authViewModel = hiltViewModel()
                 navController= rememberNavController()
                scope= rememberCoroutineScope()

                val connection by connectivityState()
                Permissions()
                    Navigation(
                        navController = navController,
                        messageViewModel,
                        authViewModel = authViewModel
                    )
                MyFirebaseMessagingService().onCreate()
                val isDialog = remember {
                    mutableStateOf(false)
                }
                if(connection == ConnectionState.Available){
                    isDialog.value = true
                }
                if(connection == ConnectionState.Unavailable) {
                        SimpleAlertDialog(
                            title = "Внимание",
                            text = "Нет доступа к интернету. Проверьте подключение к сети",
                            confirmText = "позвонить оператору",
                            dismissText = "Выход",
                            onConfirm = {
                                val callIntent: Intent = Uri
                                    .parse("tel:0666")
                                    .let { number ->
                                        Intent(Intent.ACTION_DIAL, number)
                                    }
                                this.startActivity(callIntent)
                            },
                        onDismiss = {this@MainActivity.finish()},
                        isDialog = isDialog )
                    }
            }
        }
        startSmartUserConsent()
//        checkForPermissions(Manifest.permission.ACCESS_FINE_LOCATION, "геоданным", FINE_LOCATION_RQ)
//        statusCheck()

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
            }else{
                authViewModel.updateIsAutoInsert(false)
            }
        }
    }

    private fun getOtpFromMessage(message: String, authViewModel: AuthViewModel) {
        var code = message.filter { it.isDigit() }
        Log.e("setEditValue", code)
        FCM_TOKEN?.let {
            authViewModel.setCodeAutomaticly(code, navController, scope,it)
        }
    }

    private fun registerBroadcastReceiver() {
        smsBroadcastReceiver = SmsBroadcastReceiver()
        smsBroadcastReceiver!!.smsBroadcastReceiverListener =
            object : SmsBroadcastReceiver.SmsBroadcastReceiverListener {
                override fun onSuccess(intent: Intent?) {
                    startActivityForResult(intent, REQ_USER_CONSENT)
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

//    override fun onResume() {
//        super.onResume()
//        checkForPermissions(Manifest.permission.ACCESS_FINE_LOCATION, "геоданным", FINE_LOCATION_RQ)
//        statusCheck()
//    }
//    override fun onRestart() {
//        super.onRestart()
//        checkForPermissions(Manifest.permission.ACCESS_FINE_LOCATION, "геоданным", FINE_LOCATION_RQ)
//        statusCheck()
//    }

    fun statusCheck() {
        val manager = getSystemService(LOCATION_SERVICE) as LocationManager
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()
        }
    }

    private fun buildAlertMessageNoGps() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Для продолжения работы с приложением необходимо включить GPS-приемник")
            .setCancelable(false)
            .setPositiveButton(
                "Настройки"
            ) { dialog, id ->
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                this@MainActivity.finish()
            }
            .setNegativeButton(
                "Выход"
            ) { dialog, id -> this@MainActivity.finish() }
        val alert = builder.create()
        alert.show()
    }
}

@Composable
fun noConnectionScreen(
    title: String?="",
    desc: String?=""
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(
                color = Color.Transparent,
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(25.dp, 5.dp, 25.dp, 5.dp)
                )
                .align(Alignment.Center),
        ) {

            Image(
                painter = painterResource(id = R.drawable.no_connection_icon),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth(),
                )
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = title!!,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 130.dp)
                        .fillMaxWidth(),
                    color = Color.Black,
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = desc!!,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 10.dp, start = 25.dp, end = 25.dp)
                        .fillMaxWidth(),
                    color = Color.Black,
                )

            }
        }
    }
}
@Composable
fun SimpleAlertDialog(
    title: String,
    text: String,
    confirmText: String,
    dismissText:String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    isDialog: MutableState<Boolean>
){
    if(!isDialog.value) {
        Dialog(
            onDismissRequest = { /*TODO*/ },
        ) {
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(15.dp))
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(15.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 22.sp,
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = text,
                    fontSize = 16.sp,
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(30.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Black,
                            contentColor = Color.Black
                        ),
                        onClick = { onDismiss() }
                    ) {
                        Text(text = dismissText, fontSize = 16.sp, color = Color.White)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        modifier = Modifier
                            .clickable { onConfirm() }
                            .padding(2.dp)
                            .clip(shape = RoundedCornerShape(5.dp)),
                        text = confirmText, fontSize = 18.sp, color = Color(0xFF1E88E5)
                    )
                }
            }
        }
    }
}