package com.gram.client.presentation.screens.main.components

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
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gram.client.domain.mainScreen.Address
import com.gram.client.presentation.screens.main.MainViewModel
import com.gram.client.presentation.screens.order.SearchDriverScreen
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.launch
import com.gram.client.R
import com.gram.client.utils.Constants
import com.gram.client.utils.Values
import com.gram.client.utils.getAddressText

@Composable
fun FromAddressField(fromAddress: Address, onClick: () -> Unit) {
    val mainViewModel: MainViewModel = hiltViewModel()
    val statePoint = mainViewModel.stateAddressPoint.value
    val addressName = mainViewModel.fromAddress.value.name
    Column(
        modifier = if (statePoint.isLoading) {
            Modifier
                .fillMaxWidth()
                .padding(top = 22.dp)
                .shimmer()
        } else {
            Modifier
                .fillMaxWidth()
                .padding(top = 22.dp)
        },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .width(177.dp)
                .height(50.dp)
                .clip(shape = RoundedCornerShape(percent = 50))
                .clickable {
                    onClick()
                    Values.WhichAddress.value = Constants.FROM_ADDRESS
                }
                .background(
                    if(statePoint.isLoading || addressName == "") Color.Black else Color(0xAE4CAF50),
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
            if(statePoint.isLoading){
                Text(
                    text = "Поиск", color = Color.Gray, fontSize = 11.sp,
                    fontWeight = FontWeight.Bold, maxLines = 2, overflow = TextOverflow.Ellipsis
                )
            }
            else if (fromAddress.name == "") {
                Text(
                    text = "Откуда?", color = Color.Gray, fontSize = 11.sp,
                    fontWeight = FontWeight.Bold, maxLines = 2, overflow = TextOverflow.Ellipsis
                )
            } else {
                Text(
                    text = getAddressText(fromAddress), color = Color.White, fontSize = 11.sp,
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
