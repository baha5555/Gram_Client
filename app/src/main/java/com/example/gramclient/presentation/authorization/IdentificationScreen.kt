package com.example.gramclient.presentation

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.widget.Toast
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavHostController
import com.example.gramclient.PreferencesName
import com.example.gramclient.RoutesName
import com.example.gramclient.presentation.authorization.AuthViewModel
import com.example.gramclient.presentation.components.CustomButton
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
    val context=LocalContext.current

    val focusRequesters: List<FocusRequester> = remember {
        val temp = mutableListOf<FocusRequester>()
        repeat(length) {
            temp.add(FocusRequester())
        }
        temp
    }
    coroutineScope.launch(Dispatchers.Main){
        while (time>0) {
            delay(1000L)
            time -= 1
        }
    }
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val (logo, text, codeField, text2, btn) = createRefs()

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
                    .clickable {
                        time = 25
                        coroutineScope.launch(Dispatchers.Main) {
                            while (time > 0) {
                                delay(1000L)
                                time -= 1
                            }
                        }
                    }
                    .fillMaxWidth()
                    .constrainAs(text2) {
                        top.linkTo(codeField.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .padding(top = 60.dp, bottom = 20.dp),
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
            try {
                viewModel.value.identification(code)
                navController.navigate(RoutesName.MAIN_SCREEN) {
                    popUpTo(RoutesName.IDENTIFICATION_SCREEN) {
                        inclusive = true
                    }
                }
                preferences.edit()
                    .putBoolean(PreferencesName.IS_AUTH, true)
                    .apply()
                preferences.edit()
                    .putString(PreferencesName.ACCESS_TOKEN, viewModel.value.accsess_token)
                    .apply()
            }catch (e:Exception){
                Toast.makeText(context, "Неверный код, повторите еще раз пожалуйста", Toast.LENGTH_LONG).show()
            }
        })
    }
}