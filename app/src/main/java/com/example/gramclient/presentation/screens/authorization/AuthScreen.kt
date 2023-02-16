package com.example.gramclient.presentation.screens.authorization

import android.util.Log
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.gramclient.R
import com.example.gramclient.presentation.components.CustomBackHandle
import com.example.gramclient.presentation.components.CustomButton
import com.example.gramclient.ui.theme.BackgroundColor
import com.example.gramclient.ui.theme.textSp12
import com.example.gramclient.ui.theme.textSp14
import com.example.gramclient.ui.theme.textSp18
import com.example.gramclient.utils.Constants

class AuthScreen : Screen {
    @Composable
    override fun Content() {
        //CustomBackHandle()
        val navigator = LocalNavigator.currentOrThrow

        LaunchedEffect(key1 = true){
            Log.i("asdad", "level="+navigator.level)
            Log.i("asdad", "parent="+ (navigator.parent?.lastItem?.key ?: "net"))
            Log.i("asdad", "lastItem="+navigator.lastItem.key)
            Log.i("asdad", "disposeBehavior="+navigator.disposeBehavior)

        }
        val viewModel: AuthViewModel = hiltViewModel()
        val phone = remember { mutableStateOf("") }

        val nextBtnEnabled = remember {
            mutableStateOf(false)
        }

        val mAnnotatedLinkString = buildAnnotatedString {
            addStringAnnotation(
                tag = "URL",
                annotation = Constants.KONFIG_URL,
                start = 0,
                end = 0
            )
        }

        val mUriHandler = LocalUriHandler.current

        Column(
            Modifier
                .fillMaxHeight()
                .background(color = MaterialTheme.colors.secondary),
            Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())

            ) {
                var visible by remember { mutableStateOf(false) }

                val animationTime = 1500
                val animationDelayTime = 5

                val arrowStartLocation = Offset(0F, 100F)
                val arrowEndLocation = Offset(0F, 0F)

                LaunchedEffect(Unit) {
                    visible = true
                }
                val arrowLocation by animateOffsetAsState(
                    targetValue = if (visible) arrowEndLocation else arrowStartLocation,
                    animationSpec = tween(
                        animationTime,
                        animationDelayTime,
                        easing = LinearOutSlowInEasing
                    )
                )
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.logo_gram_black),
                    "",
                    modifier = Modifier.offset(arrowLocation.x.dp, arrowLocation.y.dp),
                    tint = MaterialTheme.colors.onBackground
                )

                Column(modifier = Modifier.padding(start = 40.dp, end = 40.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(bottom = 40.dp)
                                    .background(MaterialTheme.colors.background, shape = RoundedCornerShape(5.dp))
                                    .fillMaxWidth(0.35f)
                                    .clip (RoundedCornerShape(5.dp)),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    modifier=Modifier.padding(vertical = 16.dp, horizontal = 3.dp).background(Color.White),
                                    imageVector = ImageVector.vectorResource(id = R.drawable.tj_flat),
                                    contentDescription = "Проверено"
                                )
                                Text(text = "+992", fontSize = 18.sp,modifier=Modifier.padding(vertical = 16.dp, horizontal = 3.dp))
                            }
                            OutlinedTextField(
                                value = phone.value,
                                onValueChange = {
                                    if (it.length < 10) {
                                        phone.value = it
                                        nextBtnEnabled.value = false
                                    }
                                    if (it.length == 9) nextBtnEnabled.value = true
                                },
                                placeholder = {
                                    Text(text = "Телефон", fontSize = textSp18.sp)
                                },
                                textStyle = TextStyle.Default.copy(fontSize = textSp18.sp),
                                modifier = Modifier
                                    .background(MaterialTheme.colors.background, shape = RoundedCornerShape(5.dp))
                                    .fillMaxWidth(0.95f),
                                shape = RoundedCornerShape(5.dp),
                                singleLine = true,
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    focusedBorderColor = Color(0xFF2264D1),
                                    unfocusedBorderColor = Color.Transparent,
                                    cursorColor = Color(0xFF2264D1),
                                    textColor = Color.Black,
                                ),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                            )
                        }
                    }

                    CustomButton(
                        enabled = nextBtnEnabled.value,
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .fillMaxWidth()
                            .height(61.dp),
                        text = "Продолжить",
                        textSize = 18,
                        textBold = true,
                    ) {
                        viewModel.authorization(phone.value) {
                            navigator.replace(IdentificationScreen(viewModel))
                        }
                    }
                }
            }
            Column(
                modifier = Modifier
                    .padding(10.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    textAlign = TextAlign.Center,
                    text = "Подтверждая номер телефона, я соглашаюсь с ", color = Color.Gray)
                Text(text = " правилами работы сервиса и политикой обработки персональных данных.",
                    textAlign = TextAlign.Center,
                    color = Color.Blue,
                    modifier = Modifier.clickable {
                        mAnnotatedLinkString
                            .getStringAnnotations("URL", 0, 0)
                            .firstOrNull()
                            ?.let { stringAnnotation ->
                                mUriHandler.openUri(stringAnnotation.item)
                            }
                    })
            }
        }
    }

}