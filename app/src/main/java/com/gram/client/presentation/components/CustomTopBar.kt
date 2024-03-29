package com.gram.client.presentation.components

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
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gram.client.R
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun CustomTopBar(
    title: String,
    actionNum: Int = 0,
    quitOnClick:()->Unit = {}
) {
    val navigator = LocalNavigator.currentOrThrow
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        color = Color.White
    )
    TopAppBar(
        title = { Text(title, fontSize = 18.sp) }, backgroundColor = MaterialTheme.colors.background,
        navigationIcon = {
            IconButton(onClick = { navigator.pop() }) {
                Icon(
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
                                //navController.navigate(RoutesName.SETTING_SELECT_REGION_SCREEN)
                            }
                            .padding(end = 15.dp)
                    )
                }
                2 -> {
                    IconButton(onClick = {  }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_delete_blue),
                            contentDescription = ""
                        )
                    }
                }
                3 -> {
                    IconButton(onClick =  quitOnClick ){
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_logout_blue),
                            contentDescription = ""
                        )
                    }
                }
            }
        }
    )
}

