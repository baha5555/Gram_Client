package com.gram.client.presentation.screens.drawer.myaddresses_screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gram.client.presentation.components.CustomTopAppBar


class AddAddressScreen(var type: String) : Screen {
    @Composable
    override fun Content() {
        AddAddressContent(type)
    }

}

@Composable
fun AddAddressContent(type: String) {
    val navigator = LocalNavigator.currentOrThrow

    val myAddressViewModel: MyAddressViewModel = hiltViewModel()
    val nameState = remember {
        mutableStateOf("")
    }
    val meetState = remember {
        mutableStateOf("")
    }
    val commentDriver = remember {
        mutableStateOf("")
    }

    Scaffold(topBar = {
        CustomTopAppBar(
            title = when (type) {
                "work" -> {
                    "Работа"
                }
                "home" -> {
                    "Дом"
                }
                else -> {
                    "Название адреса"
                }
            }
        )
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Column(Modifier.padding(10.dp)) {

                    CustomTextField(
                        nameState, when (type) {
                            "work" -> {
                                "Работа"
                            }
                            "home" -> {
                                "Дом"
                            }
                            else -> {
                                "Название адреса"
                            }
                        }
                    )
                    Column(
                        modifier = Modifier
                            .height(55.dp)
                            .padding(horizontal = 15.dp)
                            .fillMaxWidth(), verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Панчшанбе базар",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(text = "пр. И.Сомони 46а, подъезд 1", fontSize = 12.sp)
                    }
                    Divider()

                    CustomTextField(meetState, "Подъезд")

                    CustomTextField(commentDriver, "Комментарий водителю")
                }
                /*Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colorResource(id = R.color.background_color)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Доставка", modifier = Modifier.padding(vertical = 5.dp)
                    )
                }
                Column(Modifier.padding(10.dp)) {
                    val intercomState = remember {
                        mutableStateOf("")
                    }
                    CustomTextField(intercomState, "Домофон")
                    val text2 = remember {
                        mutableStateOf("")
                    }
                    CustomTextField(text2, "Комментарий курьеру")
                }*/
            }

            Button(
                onClick = {
                    myAddressViewModel.addMyAddress(
                        name = nameState.value,
                        search_address_id = 1,
                        meet_info = meetState.value,
                        comment = commentDriver.value,
                        type = type
                    ){
                        navigator.pop()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .height(57.dp),
                shape = RoundedCornerShape(15.dp),
                enabled = nameState.value!="" && meetState.value != "" && commentDriver.value != ""
            ) {
                Text(
                    text = "Сохранить",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }

        }
    }
}

