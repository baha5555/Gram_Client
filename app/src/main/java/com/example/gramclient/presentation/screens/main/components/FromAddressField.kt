package com.example.gramclient.presentation.screens.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.gramclient.R
import com.example.gramclient.domain.mainScreen.Address
import com.example.gramclient.presentation.screens.main.SearchAddressScreen
import com.example.gramclient.presentation.screens.order.SearchDriverScreen
import kotlinx.coroutines.launch


@Composable
fun FromAddressField(fromAddress: Address, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 22.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .width(177.dp)
                .height(50.dp)
                .clip(shape = RoundedCornerShape(percent = 50))
                .clickable {
                    onClick()
                }
                .background(
                    Color.Black,
                    shape = RoundedCornerShape(percent = 50)
                )
                .padding(horizontal = 15.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Ваш адрес", color = Color.White, fontSize = 11.sp)
                Spacer(modifier = Modifier.width(5.dp))
                Icon(
                    modifier = Modifier,
                    painter = painterResource(R.drawable.arrow_right),
                    contentDescription = "icon",
                    tint = Color.White
                )
            }
            if (fromAddress.address == "") {
                Text(
                    text = "Откуда?", color = Color.Gray, fontSize = 11.sp,
                    fontWeight = FontWeight.Bold, maxLines = 2, overflow = TextOverflow.Ellipsis
                )
            } else {
                Text(
                    text = fromAddress.address ?: "", color = Color.White, fontSize = 11.sp,
                    fontWeight = FontWeight.Bold, maxLines = 2, overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
@Composable
fun ArrowBack() {
    val navigator = LocalNavigator.currentOrThrow
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 10.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        FloatingActionButton(
            modifier = Modifier
                .size(50.dp),
            backgroundColor = Color.White,
            onClick = {
                coroutineScope.launch {
                    navigator.replaceAll(SearchDriverScreen())
                }
            }
        ) {
            Icon(
                Icons.Filled.ArrowBack,
                contentDescription = "Menu", tint = Color.Black,
                modifier = Modifier.size(25.dp)
            )
        }
    }
}
