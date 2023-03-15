package com.gram.client.presentation.screens.promocod

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gram.client.R
import com.gram.client.presentation.components.CustomButton
import com.gram.client.ui.theme.PrimaryColor

class PromocodScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val promocodViewModel: PromocodViewModel = hiltViewModel()
        LaunchedEffect(key1 = true)
        {
            promocodViewModel.getPromocod()
        }
        Scaffold(
            topBar = {
                Row(
                    modifier = Modifier.fillMaxHeight(0.1f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TopAppBar(
                        content = {
                            Box(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                IconButton(modifier = Modifier, onClick = { navigator.pop() }) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_back_blue),
                                        contentDescription = ""
                                    )
                                }
                                Text(
                                    text = "Промокоды",
                                    textAlign = TextAlign.Center,
                                    color = Color.Black,
                                    modifier = Modifier.align(Alignment.Center),
                                    fontSize = 25.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        },
                        backgroundColor = Color.White,
                        contentColor = Color.Black,
                        elevation = 0.dp
                    )
                }
            }, backgroundColor = Color.White

        ) {
            Column {
                Column(Modifier.clickable {}) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 15.dp, top = 25.dp, bottom = 25.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                imageVector = ImageVector.vectorResource(id = R.drawable.coupon_icon),
                                contentDescription = ""
                            )
                            Spacer(modifier = Modifier.width(15.dp))
                            Text(
                                text = "Ввести промокод",
                                fontSize = 16.sp,
                                color = Color(0xFF565E66),
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Image(
                            modifier = Modifier.size(24.dp),
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_back_forward_blue),
                            contentDescription = ""
                        )
                    }
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 25.dp)
                    )

                }
                Column(Modifier.clickable {navigator.push(ShareScreen())}) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 15.dp, top = 25.dp, bottom = 25.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                imageVector = ImageVector.vectorResource(id = R.drawable.gift_icon),
                                contentDescription = ""
                            )
                            Spacer(modifier = Modifier.width(15.dp))
                            Text(
                                text = "Дари друзьям",
                                fontSize = 16.sp,
                                color = Color(0xFF565E66),
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Image(
                            modifier = Modifier.size(24.dp),
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_back_forward_blue),
                            contentDescription = ""
                        )
                    }
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 25.dp)
                    )
                }
            }


        }
    }
}

fun shareText(text: String, launcher: ActivityResultLauncher<Intent>) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }

    val chooser = Intent.createChooser(sendIntent, "Поделиться")
    launcher.launch(chooser)
}


/*Column {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(20.dp)
    ) {
        Text(text = "Поделись своим кодом:", fontSize = 18.sp)
        val search = remember {
            mutableStateOf("Y045KG")
        }
        TextField(
            enabled = true,
            value = search.value,
            onValueChange = {
                search.value = it
            },
            placeholder = { Text(text = "Ввести промокод") },
            trailingIcon = {
                if (search.value.isNotEmpty())
                    IconButton(
                        onClick = {

                        },
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.arrow_right),
                            contentDescription = "",
                            tint = PrimaryColor,
                            modifier = Modifier.size(28.dp)
                        )
                    }
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                cursorColor = Color(0xFF005EFF),
                focusedIndicatorColor = Color(0xFF005EFF),
                leadingIconColor = Color(0xFF005EFF),
                disabledTextColor = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth(),
            textStyle = TextStyle(
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            ),
        )
        Spacer(modifier = Modifier.height(30.dp))

        IconButton(onClick = {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }) {
        Icon(Icons.Filled.Share, contentDescription = "Поделиться")
    }

    }
}*/