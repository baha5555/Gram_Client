package com.gram.client.presentation.screens.promocod

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gram.client.R

class PromocodScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val promocodeViewModel: PromocodViewModel = hiltViewModel()
        val bottomNavigator = LocalBottomSheetNavigator.current

        LaunchedEffect(key1 = true)
        {
            promocodeViewModel.getPromoCode()
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
                Column(Modifier.clickable {bottomNavigator.show(SendPromoCodeScreen())}) {
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