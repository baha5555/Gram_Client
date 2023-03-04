package com.example.gramclient.presentation.components.voyager

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import com.example.gramclient.R

class SearchAddresses : Screen {
    @Composable
    override fun Content() {
        val fromText = remember {
            mutableStateOf("")
        }
        val toText = remember {
            mutableStateOf("")
        }
        val fromIsFocused = remember {
            mutableStateOf(true)
        }
        val toIsFocused = remember {
            mutableStateOf(true)
        }
        Column(modifier = Modifier.padding(20.dp)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(16.dp, RoundedCornerShape(15.dp))
                    .clip(
                        RoundedCornerShape(15.dp)
                    )
                    .background(
                        Color.White
                    )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        imageVector = ImageVector.vectorResource(if(fromIsFocused.value) R.drawable.ic_serach_address else R.drawable.ic_serach_address_from),
                        contentDescription = "",
                        modifier = Modifier.padding(start = 15.dp)
                    )
                    TextField(
                        value = fromText.value,
                        onValueChange = { fromText.value = it },
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = Color.Black,
                            disabledTextColor = Color.Transparent,
                            backgroundColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        textStyle = TextStyle(fontSize = 18.sp),
                        modifier = Modifier
                            .weight(1f)
                            .onFocusChanged {
                                fromIsFocused.value = it.isFocused
                            },
                        maxLines = 1
                    )
                    if (fromIsFocused.value) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (fromText.value != "") {
                                ClearText(fromText)
                            }
                            Divider(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(35.dp)
                            )
                            Text(text = "Карта",
                                modifier = Modifier
                                    .padding(start = 10.dp, end = 15.dp)
                                    .clickable {

                                    })
                        }
                    }
                }
                Divider(modifier = Modifier.padding(start = 45.dp, end = 15.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        imageVector = ImageVector.vectorResource(if(toIsFocused.value) R.drawable.ic_serach_address else R.drawable.ic_serach_address_to),
                        contentDescription = "",
                        modifier = Modifier.padding(start = 15.dp)
                    )
                    TextField(
                        value = toText.value,
                        onValueChange = { toText.value = it },
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = Color.Black,
                            disabledTextColor = Color.Transparent,
                            backgroundColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        textStyle = TextStyle(fontSize = 18.sp),
                        modifier = Modifier
                            .weight(1f)
                            .onFocusChanged {
                                toIsFocused.value = it.isFocused
                            },
                        maxLines = 1
                    )
                    if (toIsFocused.value) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (toText.value != "") {
                                ClearText(toText)
                            }
                            Divider(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(35.dp)
                            )
                            Text(text = "Карта",
                                modifier = Modifier
                                    .padding(start = 10.dp, end = 15.dp)
                                    .clickable {

                                    })
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun ClearText(text: MutableState<String>) {
        IconButton(onClick = {
            text.value = ""
        }) {
            Icon(
                Icons.Default.Close, contentDescription = ""
            )
        }
    }
}