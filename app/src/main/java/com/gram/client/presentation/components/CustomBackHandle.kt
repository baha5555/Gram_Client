package com.gram.client.presentation.components

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.gram.client.presentation.MainActivity

@Composable
fun CustomBackHandle(enabled: Boolean = true){
    var pressedTime: Long = 0
    val activity = (LocalContext.current as? MainActivity)
    val context= LocalContext.current
    BackHandler(enabled = enabled) {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            activity?.finish()
        } else {
            Toast.makeText(context, "Нажмите еще раз, чтобы выйти", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }
}