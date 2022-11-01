package com.example.gramclient.presentation

import android.annotation.SuppressLint
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.gramclient.R
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.gramclient.RoutesName
import com.example.gramclient.presentation.components.CustomButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun IdentificationScreen(
    modifier: Modifier = Modifier,
    length: Int = 4,
    onFilled: (code: String) -> Unit,
    navController: NavHostController
) {
    var code: List<Char> by remember{ mutableStateOf(listOf())}
    var time: Int by remember{ mutableStateOf(25)}

    val coroutineScope= rememberCoroutineScope()

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
            time=time-1
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
            bitmap = ImageBitmap.imageResource(R.drawable.logo),
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
            Text(text = "+992 92999-99-99", modifier=Modifier.fillMaxWidth(), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
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
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
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
                                time = time - 1
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

        Button(
            onClick = {
                navController.navigate(RoutesName.MAIN_SCREEN)
            },
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
            enabled = if(code.size==4) true else false,
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF2264D1), contentColor = Color.White),
            content = { Text(text = "Подтвердить", fontWeight = FontWeight.Bold, fontSize = 18.sp, lineHeight = 28.sp) },
        )
    }

}