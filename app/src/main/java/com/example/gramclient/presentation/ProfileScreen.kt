package com.example.gramclient.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.gramclient.R
import com.example.gramclient.presentation.components.CustomTopBar

@Composable
fun ProfileScreen(
    navController: NavHostController
){
    Scaffold(
        topBar = { CustomTopBar(title = "Профиль", navController = navController ) }
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(Color(0xFFEBEBEB))
                .fillMaxSize()) {
            IconButton(
                onClick = { /*TODO*/ }
            , modifier = Modifier
                    .padding(top = 21.dp)
                    .size(90.dp)
                    .background(Color.White, shape = RoundedCornerShape(50.dp))

            ) {
                Image(
                    modifier = Modifier
                        .width(60.dp)
                        .height(60.dp),
                    imageVector = ImageVector.vectorResource(id = R.drawable.camera_plus),
                    contentDescription = "",
                )
            }
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 75.dp, start = 27.dp, end = 21.dp)
            ) {
            }
        }
    }
}
