package com.gram.client.presentation.screens.drawer.myaddresses_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gram.client.R
import com.gram.client.presentation.components.CustomTopAppBar


class EditAddressScreen(
    var name: String?,
    var searchAddressId: Int,
    var meetInfo: String?,
    var commentToDriver: String?,
    var type: String,
    var id: Int
) : Screen {

    @Composable
    fun DeleteMyAddress(function: () -> Unit) {
        IconButton(onClick = {
            function.invoke()
        }) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_delete_blue),
                contentDescription = "back"
            )
        }
    }

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val myAddressViewModel: MyAddressViewModel = hiltViewModel()
        val nameState = remember {
            mutableStateOf(""+name)
        }
        val meetState = remember {
            mutableStateOf(""+meetInfo)
        }
        val commentDriver = remember {
            mutableStateOf( "")
        }
        LaunchedEffect(key1 = true ){
            if(commentToDriver!=null){
                commentDriver.value += commentToDriver
            }
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
                        name
                    }
                }
            ) {
                DeleteMyAddress() {
                    myAddressViewModel.deleteMyAddress(id) {
                        navigator.pop()
                    }
                }
            }
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
                    shape = RoundedCornerShape(15.dp)
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

}
