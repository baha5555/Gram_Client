package com.example.gramclient.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavHostController
import com.example.gramclient.R
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun CustomTopBar(title: String, navController: NavHostController) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        color = Color.White
    )
    TopAppBar(title = { Text(title) }, backgroundColor = Color.White, navigationIcon = {
        IconButton(onClick = {navController.popBackStack() }) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.btn_back_icon),
                contentDescription = ""
            )
        }
    })
}

