package com.gram.client.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gram.client.R

@Composable
fun CustomTopAppBar(
    title: String? = "",
    action: @Composable() (() -> Unit)? = null
) {
    val navigator = LocalNavigator.currentOrThrow

    TopAppBar(backgroundColor = colorResource(id = R.color.white), elevation = 0.dp) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            //Title
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                if (title != null) {
                    Text(title, fontSize = 20.sp, color = colorResource(id = R.color.black))
                }
            }
            //Back
            Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.fillMaxWidth()) {
                IconButton(onClick = {
                    navigator.pop()
                }) {
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

