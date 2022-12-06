package com.example.gramclient.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.gramclient.R

@Composable
fun CustomTopAppBar(
    title: String = "",
    action: @Composable() (() -> Unit)? = null,
    navController: NavHostController
) {
    TopAppBar(backgroundColor = colorResource(id = R.color.white)) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            //Title
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                Text(title, fontSize = 18.sp, color = colorResource(id = R.color.black))
            }
            //Back
            Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.fillMaxWidth()) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Image(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_back),
                        contentDescription = "back"
                    )
                }
            }
            //Action
            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                if (action != null) {
                    action.invoke()
                }
            }
        }
    }
}

