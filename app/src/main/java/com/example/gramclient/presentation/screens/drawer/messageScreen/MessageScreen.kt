package com.example.gramclient.presentation.screens.drawer.messageScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.gramclient.R
import com.example.gramclient.presentation.components.CustomTopBar
import com.example.gramclient.ui.theme.BackgroundColor
import com.example.gramclient.ui.theme.PrimaryColor


@Composable
fun MessageScreen(
    navController: NavHostController,
    viewModel: Lazy<MessageViewModel>
){
    val message = remember { mutableStateOf("") }
    val messages=viewModel.value.messages.observeAsState()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        backgroundColor = BackgroundColor,
        topBar = { CustomTopBar(title = "Поддержка")},
        bottomBar = {
            BottomAppBar(
                modifier= Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                backgroundColor = Color.White,
                elevation = 5.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    IconButton(onClick = {  }) {
                        Image(
                            modifier = Modifier
                                .weight(1f)
                                .size(25.dp),
                            painter = painterResource(R.drawable.route_icon),
                            contentDescription = "icon"
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    OutlinedTextField(
                        value = message.value,
                        onValueChange = {
                            message.value = it
                        },
                        placeholder = {
                            Text(text = "Сообщение", fontSize = 16.sp)
                        },
                        textStyle = TextStyle.Default.copy(fontSize = 16.sp),
                        modifier = Modifier
                            .weight(7f)
                            .width(256.dp)
                            .height(51.dp)
                            .background(
                                Color(0xFFCED6DE),
                                shape = RoundedCornerShape(
                                    topStart = 20.dp,
                                    topEnd = 0.dp,
                                    bottomStart = 20.dp,
                                    bottomEnd = 20.dp
                                )
                            ),
                        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 0.dp, bottomStart = 20.dp, bottomEnd = 20.dp),
                        singleLine = true,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color(0xFF2264D1),
                            unfocusedBorderColor = Color.Transparent,
                            cursorColor = Color(0xFF000000),
                            textColor = Color.Black,
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        keyboardActions = KeyboardActions {
                            val sendMessage=message.value.trim()
                            viewModel.value.sendMessage(sendMessage)
                            message.value = ""
                             }
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    IconButton(onClick = {
                        val sendMessage=message.value.trim()
                        viewModel.value.sendMessage(sendMessage)
                        message.value = ""
                    }) {
                        Image(
                            modifier = Modifier
                                .weight(1f)
                                .size(30.dp),
                            painter = painterResource(R.drawable.send_icon),
                            contentDescription = "icon"
                        )
                    }
                }
            }
        },
        content = {
            LazyColumn(modifier = Modifier
                .fillMaxSize()
                .padding(top = 5.dp, bottom = 90.dp, start = 5.dp, end = 5.dp), horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.Bottom)
            {
                messages.value?.forEach { mess ->
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            horizontalAlignment = Alignment.End
                        ) {
                            Card(
                                modifier = Modifier.widthIn(max = 340.dp),
                                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp), // 3
                                backgroundColor = PrimaryColor,
                            ) {
                                Text(
                                    modifier = Modifier.padding(8.dp),
                                    text = mess,
                                    color = Color.White
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                    }
                }
            }
        }
    )
}
