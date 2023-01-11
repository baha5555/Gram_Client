package com.example.gramclient.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.gramclient.R
import com.example.gramclient.utils.RoutesName
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
        title = { Text(title, fontSize = 18.sp) }, backgroundColor = Color.White,
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_back_blue),
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
                    IconButton(onClick = {  }) {
                        Image(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_delete_blue),
                            contentDescription = ""
                        )
                    }
                }
                3 -> {
                    IconButton(onClick = {  }) {
                        Image(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_logout_blue),
                            contentDescription = ""
                        )
                    }
                }
            }
        }
    )
}

