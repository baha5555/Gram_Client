package com.example.gramclient.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.gramclient.R
import com.example.gramclient.presentation.components.CustomSwitch
import com.example.gramclient.presentation.components.CustomTopBar
import com.example.gramclient.ui.theme.BackgroundColor
import com.example.gramclient.ui.theme.FontSilver

@Composable
fun ProfileScreen(
    navController: NavHostController
) {
    Scaffold(
        topBar = { CustomTopBar(title = "Профиль", navController = navController, actionNum = 3) }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(BackgroundColor)
                .fillMaxSize()
        ) {
            IconButton(
                onClick = { /*TODO*/ }, modifier = Modifier
                    .padding(top = 21.dp)
                    .size(90.dp)
                    .background(Color.White, shape = RoundedCornerShape(50.dp))

            ) {
                Image(
                    modifier = Modifier
                        .width(60.dp)
                        .height(60.dp),
                    imageVector = ImageVector.vectorResource(id = R.drawable.camera_plus),
                    contentDescription = "",
                )
            }
            Spacer(modifier = Modifier.height(75.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 27.dp, end = 21.dp)
            ) {
                val profileName = remember { mutableStateOf("") }
                val profileEmail = remember { mutableStateOf("") }
                val profilePhone = remember { mutableStateOf("") }
                TextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = profileName.value,
                    onValueChange = { profileName.value = it },
                    label = {Text(text = "Имя*") },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = BackgroundColor,
                        unfocusedLabelColor = FontSilver,
                        focusedLabelColor = FontSilver,
                        unfocusedIndicatorColor = FontSilver,
                        focusedIndicatorColor = FontSilver,
                        cursorColor = FontSilver,
                    )
                )
                Spacer(modifier = Modifier.height(35.dp))
                TextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = profileEmail.value,
                    onValueChange = { profileEmail.value = it },
                    label = {Text(text = "Email") },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = BackgroundColor,
                        unfocusedLabelColor = FontSilver,
                        focusedLabelColor = FontSilver,
                        unfocusedIndicatorColor = FontSilver,
                        focusedIndicatorColor = FontSilver,
                        cursorColor = FontSilver,
                    )
                )
                Spacer(modifier = Modifier.height(35.dp))
                TextField(
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = profilePhone.value,
                    onValueChange = { profilePhone.value = it },
                    label = {Text(text = "Телефон") },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = BackgroundColor,
                        unfocusedLabelColor = FontSilver,
                        focusedLabelColor = FontSilver,
                        unfocusedIndicatorColor = FontSilver,
                        focusedIndicatorColor = FontSilver,
                        cursorColor = FontSilver,
                    )
                )
                Spacer(modifier = Modifier.height(49.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween

                ) {
                    Text(text = "Получение рассылки", color = Color.Black, fontSize = 14.sp)
                 Box(modifier = Modifier.padding(end = 5.dp)) {
                     val switchON = remember {
                         mutableStateOf(false) // Initially the switch is ON
                     }
                     CustomSwitch(switchON)
                 }

                }
            }
        }
    }
}

