package com.example.gramclient.presentation

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.gramclient.R
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavHostController
import com.example.gramclient.PreferencesName
import com.example.gramclient.RoutesName
import com.example.gramclient.presentation.authorization.AuthViewModel
import com.example.gramclient.presentation.authorization.states.IdentificationResponseState
import com.example.gramclient.presentation.components.CustomButton
import com.example.gramclient.ui.theme.PrimaryColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition", "SuspiciousIndentation")
@Composable
fun IdentificationScreen(
    modifier: Modifier = Modifier,
    length: Int = 4,
    onFilled: (code: String) -> Unit,
    navController: NavHostController,
    preferences: SharedPreferences,
    viewModel: Lazy<AuthViewModel>
) {
    var code: List<Char> by remember{ mutableStateOf(listOf())}
    var time: Int by remember{ mutableStateOf(25)}

    val coroutineScope= rememberCoroutineScope()
    val stateLogin by viewModel.value.stateLogin


    val focusRequesters: List<FocusRequester> = remember {
        val temp = mutableListOf<FocusRequester>()
        repeat(length) {
            temp.add(FocusRequester())
        }
        temp
    }
    LaunchedEffect(key1 = true ){
        while (time>0) {
            delay(1000L)
            time -= 1
        }
    }

    LoadingIndicator(stateLogin.isLoading)


    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val (logo, text, codeField, text2, btn, error) = createRefs()

        Image(
            modifier= Modifier
                .constrainAs(logo) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                }
                .padding(top = 155.dp)
                .width(176.dp)
                .height(50.07.dp),
            imageVector = ImageVector.vectorResource(R.drawable.logo_gram_black),
            contentDescription = "Logo"
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(text) {
                    top.linkTo(logo.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(top = 47.dp)
        ){
            Text(text = "Сообщение с кодом отправлено на", modifier=Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            Text(text = "+992${viewModel.value.phoneNumber}", modifier=Modifier.fillMaxWidth(), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
        }

        Row(modifier = modifier
            .constrainAs(codeField) {
                top.linkTo(text.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
            .padding(top = 65.dp)
        ) {
            (0 until length).forEach { index ->
                OutlinedTextField(
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 2.dp)
                        .focusRequester(focusRequesters[index]),
                    textStyle = MaterialTheme.typography.h4.copy(textAlign = TextAlign.Center),
                    singleLine = true,
                    value = code.getOrNull(index)?.takeIf { it.isDigit() }?.toString() ?: "",
                    onValueChange = { value: String ->
                        if (focusRequesters[index].freeFocus()) {
                            val temp = code.toMutableList()
                            if (value == "") {
                                if (temp.size > index) {
                                    temp.removeAt(index)
                                code=temp
                                    focusRequesters.getOrNull(index - 1)?.requestFocus()
                                }
                            } else {
                                if (code.size > index) {
                                    temp[index] = value.getOrNull(0) ?: ' '
                                } else if (value.getOrNull(0)?.isDigit() == true) {
                                    temp.add(value.getOrNull(0) ?: ' ')
                                code = temp
                                    focusRequesters.getOrNull(index + 1)?.requestFocus() ?: onFilled(
                                        code.joinToString(separator = "")
                                    )
                                }
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Number
                    ),
                    )
                Spacer(modifier = Modifier.width(16.dp))
            }
        }
        ErrorMessage(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(error) {
                    top.linkTo(codeField.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(top = 20.dp),
            message = stateLogin.error
        )
        if(time>0) {
            Text(text = "Повторный запрос кода: 00:$time",
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(text2) {
                        top.linkTo(codeField.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .padding(top = 60.dp, bottom = 20.dp),
                textAlign = TextAlign.Center, color = Color.Gray)
        }else{
            Text(text = "Отправить код еще раз",
                modifier= Modifier
                    .fillMaxWidth()
                    .constrainAs(text2) {
                        top.linkTo(codeField.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .padding(top = 60.dp, bottom = 20.dp)
                    .clickable {
                        time = 25
                        coroutineScope.launch(Dispatchers.Main) {
                            while (time > 0) {
                                delay(1000L)
                                time -= 1
                            }
                        }
                        viewModel.value.authorization(viewModel.value.phoneNumber.toInt())
                    },
                textAlign = TextAlign.Center, color = Color.Blue)
        }
        CustomButton(
            modifier = Modifier
                .constrainAs(btn) {
                    top.linkTo(text2.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .clip(RoundedCornerShape(5.dp))
                .background(Color.Black)
                .width(303.dp)
                .height(54.dp)
                .padding(top = 0.dp),
            text = "Подтвердить",
            textSize = 18,
            textBold = true,
            enabled = code.size==4,
        onClick = {
            viewModel.value.identification(code, preferences, navController)
        })
    }
}

@Composable
fun ErrorMessage(
    modifier: Modifier,
    message: String
) {
    if (message != "") {
        Text(
            modifier=modifier,
            text = message,
            textAlign = TextAlign.Center,
            color = Color(0xFFF44336)
        )
    }
}
@Composable
fun LoadingIndicator(isLoading: Boolean, backgroundColor: Color = Color(0x00E5E5E5)) {
    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = PrimaryColor)
        }
    }
}