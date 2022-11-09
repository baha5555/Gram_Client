package com.example.gramclient.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.gramclient.R
import com.example.gramclient.RoutesName
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun CustomTopBar(
    title: String,
    navController: NavHostController,
    actionNum: Int = 0
) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        color = Color.White
    )
    TopAppBar(
        title = { Text(title) }, backgroundColor = Color.White,
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.btn_back_icon),
                    contentDescription = ""
                )
            }
        },
        actions = {
            when (actionNum) {
                1 -> {
                    Text(
                        "Таджикистан",
                        fontSize = 18.sp,
                        modifier = Modifier
                            .clickable {
                                navController.navigate(RoutesName.SETTING_SELECT_REGION_SCREEN)
                            }
                            .padding(end = 15.dp)
                    )
                }
                2 -> {
                    Image(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "",

                    )
                }
            }
        }
    )
}

