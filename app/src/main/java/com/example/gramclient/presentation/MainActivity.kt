package com.example.gramclient.presentation

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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

    val FINE_LOCATION_RQ = 101
    val CAMERA_RQ = 101

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
        checkForPermissions(Manifest.permission.ACCESS_FINE_LOCATION, "локации", FINE_LOCATION_RQ)
        preferences=getSharedPreferences(PreferencesName.APP_PREFERENCES, Context.MODE_PRIVATE)
    }

    private fun checkForPermissions(permission: String, name:String, requestCode:Int){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            when{
                ContextCompat.checkSelfPermission(applicationContext, permission) == PackageManager.PERMISSION_GRANTED ->{
                    Toast.makeText(this, "Разрешение получено", Toast.LENGTH_SHORT).show()
                }
                shouldShowRequestPermissionRationale(permission) -> showDialog(permission, name, requestCode)
                else -> ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        fun innerCheck(name: String){
            if(grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED){
                showDialog(Manifest.permission.ACCESS_FINE_LOCATION, name, requestCode)
            }else{
                Log.d("locationPermission", "Разрешение на геолокацию получено")
            }
        }
        when(requestCode){
            FINE_LOCATION_RQ -> innerCheck("локации")
            CAMERA_RQ -> innerCheck("camera")
        }
    }

    private fun showDialog(permission: String, name: String, requestCode: Int){
        val builder=AlertDialog.Builder(this)

        builder.apply {
            setMessage("Для использования этого приложения требуется разрешение на доступ к вашей $name")
            setTitle("Требуется разрешение")
            setPositiveButton("OK"){ dialog, which ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            setNegativeButton("Отмена"){dialog, which ->
                this@MainActivity.finish()
                //delete all caches
//                (context.getSystemService(ACTIVITY_SERVICE) as ActivityManager).clearApplicationUserData()
            }
        }
        val dialog = builder.create()
        dialog.show()
    }

}