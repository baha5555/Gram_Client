package com.example.gramclient.presentation.screens.authorization

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.gramclient.R
import com.example.gramclient.presentation.components.*
import com.example.gramclient.presentation.screens.main.MainScreen
import com.example.gramclient.ui.theme.PrimaryColor
import com.example.gramclient.utils.Constants.FCM_TOKEN
import com.example.gramclient.utils.RoutesName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class IdentificationScreen(val viewModel: AuthViewModel) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val modifier: Modifier = Modifier
        var time: Int by remember{ mutableStateOf(25)}
        val code=viewModel.smsCode.observeAsState()
        val phone=viewModel.phoneNumber.observeAsState()

        val coroutineScope= rememberCoroutineScope()
        val stateLogin by viewModel.stateLogin
        val stateAuth by viewModel.stateAuth

        var initialApiCalled by rememberSaveable { mutableStateOf(false) }
        val isAutoInsert by viewModel.isAutoInsert

        LaunchedEffect(key1 = true ){
            while (time>0) {
                delay(1000L)
                time -= 1
            }
        }
        if(stateLogin.isLoading) {
            LoadingIndicator(isLoading = true)
        }
        else{
            stateAuth.response?.let{
                if (!initialApiCalled) {
                    LaunchedEffect(code.value) {
                        if (code.value!!.length == 4 && !isAutoInsert) {
                            FCM_TOKEN?.let { fcm_token ->
                                viewModel.identification(
                                    code.value!!,
                                    it.result.client_register_id,
                                    fcm_token
                                ){
                                    navigator.replaceAll(MainScreen())
                                }
                            }
                            initialApiCalled = true
                        }
                    }
                }
                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    val (logo, text, codeField, text2, error) = createRefs()
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
                        Text(text = "+992${phone.value}", modifier=Modifier.fillMaxWidth(), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
                    }

                    Row(modifier = modifier
                        .constrainAs(codeField) {
                            top.linkTo(text.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .padding(top = 20.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        RegistrationCodeInput(
                            codeLength = 4,
                            initialCode = code.value!!
                        ) {
                            viewModel.updateCode(it)
                            if(it.length ==4){
                                initialApiCalled = false
                            }
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
                    }
                    else{
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
                                    phone.value?.let { it1 ->
                                        viewModel.authorization(
                                            it1.toInt()
                                        ){
                                            navigator.replace(IdentificationScreen(viewModel))
                                        }
                                    }
                                },
                            textAlign = TextAlign.Center, color = Color.Blue)
                    }
                }
            }
        }
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
fun LoadingIndicator(isLoading: Boolean) {
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

