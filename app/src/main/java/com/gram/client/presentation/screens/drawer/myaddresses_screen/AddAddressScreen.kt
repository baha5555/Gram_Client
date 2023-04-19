package com.gram.client.presentation.screens.drawer.myaddresses_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
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
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gram.client.domain.mainScreen.Address
import com.gram.client.domain.myAddresses.AddMyAddressRequest
import com.gram.client.presentation.components.CustomTopAppBar
import com.gram.client.presentation.components.voyager.SearchAddressNavigator
import com.gram.client.utils.Constants


class AddAddressScreen(var type: String) : Screen {
    @Composable
    override fun Content() {
        AddAddressContent(type)
    }

}

@SuppressLint("UnrememberedMutableState")
@Composable
fun AddAddressContent(type: String) {
    val navigator = LocalNavigator.currentOrThrow
    val bottomNavigator = LocalBottomSheetNavigator.current

    val stateAddress = mutableStateOf(Address())

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
                            .clickable {
                                bottomNavigator.show(
                                    SearchAddressNavigator(
                                        whichScreen = Constants.MY_ADDRESS,
                                        stateAddress = stateAddress
                                    ) {

                                    })
                            }
                            .padding(horizontal = 15.dp)
                            .fillMaxWidth(), verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = if (stateAddress.value.address == "") "Выбрать адрес" else (stateAddress.value.address),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        //Text(text = "Добавить адрес", fontSize = 12.sp)
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
                        AddMyAddressRequest(
                            name = nameState.value,
                            search_address_id = stateAddress.value.id,
                            meet_info = if(meetState.value != "") meetState.value else null,
                            comment_to_driver = if(commentDriver.value!= "") commentDriver.value else null,
                            type = type
                        )
                    ) {
                        navigator.pop()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .height(57.dp),
                shape = RoundedCornerShape(15.dp),
                enabled = nameState.value != "" && stateAddress.value.id != 0
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

