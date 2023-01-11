package com.example.gramclient.presentation.screens.drawer.myaddresses_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.gramclient.R
import com.example.gramclient.presentation.components.CustomTopAppBar

@Composable
fun EditAddressScreen(navController: NavHostController) {
    Scaffold(topBar = {
        CustomTopAppBar(
            title = "Название адреса", navController = navController
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
                    val nameState = remember {
                        mutableStateOf("")
                    }
                    CustomTextField(nameState, "Название")
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
                    val entranceState = remember {
                        mutableStateOf("")
                    }
                    CustomTextField(entranceState, "Подъезд")
                    val commentDriver = remember {
                        mutableStateOf("")
                    }
                    CustomTextField(commentDriver, "Комментарий водителю")
                }
                Box(
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
                }
            }

            Button(
                onClick = { /*TODO*/ },
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

@Composable
fun CustomTextField(textState: MutableState<String>, label: String) {
    TextField(
        value = textState.value,
        onValueChange = { newText ->
            textState.value = newText
        },
        label = { Text(text = label) },
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            textColor = Color.Gray,
            disabledTextColor = Color.Transparent,
            unfocusedIndicatorColor = Color(0xFFDEDEDE),
            disabledIndicatorColor = Color.Transparent
        ),
    )
}