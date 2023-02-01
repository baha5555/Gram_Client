package com.example.gramclient.presentation.components

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.gramclient.presentation.MainActivity

@Composable
fun CustomBackHandle(){
    var pressedTime: Long = 0
    val activity = (LocalContext.current as? MainActivity)
    val context= LocalContext.current
    BackHandler(enabled = true) {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            activity?.finish()
        } else {
            Toast.makeText(context, "Нажмите еще раз, чтобы выйти", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }
}