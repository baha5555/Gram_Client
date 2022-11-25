package com.example.gramclient.presentation

import android.content.SharedPreferences
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.gramclient.PreferencesName
import com.example.gramclient.R
import com.example.gramclient.presentation.components.CustomButton
import com.example.gramclient.presentation.components.CustomDropDownMenu
import com.example.gramclient.presentation.components.CustomSwitch
import com.example.gramclient.presentation.components.CustomTopBar
import com.example.gramclient.presentation.profile.ProfileViewModel
import com.example.gramclient.ui.theme.BackgroundColor
import com.example.gramclient.ui.theme.FontSilver

@Composable
fun ProfileScreen(
    navController: NavHostController,
    viewModel: Lazy<ProfileViewModel>,
    preferences: SharedPreferences,
) {
    val stateGetProfileInfo by viewModel.value.stateGetProfileInfo
    val profileFirstName = remember { mutableStateOf(stateGetProfileInfo.response?.first_name) }
    val profileEmail = remember { mutableStateOf(stateGetProfileInfo.response?.email) }
    val profileLastName = remember { mutableStateOf(stateGetProfileInfo.response?.last_name) }

    val selectedTextGender = remember {
        mutableStateOf(
            when (viewModel.value.genderId.value) {
                1 -> "Мужской"
                2 -> "Женский"
                else -> "Пол"
            }
        )
    }

        viewModel.value.getProfileInfo(preferences.getString(PreferencesName.ACCESS_TOKEN, "").toString())
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

                TextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = profileFirstName.value.toString(),
                    onValueChange = { profileFirstName.value = it },
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
//                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = profileLastName.value.toString(),
                    onValueChange = { profileLastName.value = it },
                    label = {Text(text = "Фамилия") },
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
                    value = profileEmail.value.toString(),
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
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = viewModel.value.stateGetProfileInfo.value.response?.phone.toString(),
                    onValueChange = {  },
                    label = {Text(text = "Телефон") },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = BackgroundColor,
                        unfocusedLabelColor = FontSilver,
                        focusedLabelColor = FontSilver,
                        unfocusedIndicatorColor = FontSilver,
                        focusedIndicatorColor = FontSilver,
                        cursorColor = FontSilver,
                    ),
                    textStyle = TextStyle(color = Color.Gray)
                )

                Spacer(modifier = Modifier.height(49.dp))
//                CustomDropDownMenu(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 32.dp),
//                    selectedText = selectedTextGender.value,
//                    suggestions = viewModel.value.stateListOfGenders.value,
//                    onClick = {
//                        selectedTextGender.value = it
//                        when (it) {
//                            viewModel.value.stateListOfGenders.value[0] -> {
//                                viewModel.value.setGenderId(1)
//                            }
//                            viewModel.value.stateListOfGenders.value[1] -> {
//                                viewModel.value.setGenderId(0)
//                            }
//                        }
//
//                    }
//                )
//                Spacer(modifier = Modifier.height(49.dp))

                Row(modifier=Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.Center) {
                    CustomButton(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.Black)
                            .width(363.dp)
                            .height(55.dp)
                            .padding(top = 0.dp),
                        text = "Cохранить",
                        textSize = 18,
                        textBold = true,
                        onClick = {
                            viewModel.value.sendProfile(
                                preferences.getString(PreferencesName.ACCESS_TOKEN, "").toString(),
                                profileFirstName.value!!,
                                profileLastName.value!!,
                                "0",
                                "2022-01-01",
                                profileEmail.value!!
                            )
                        })
                    Spacer(modifier = Modifier.height(49.dp))
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween

                ) {
                    Text(text = "Получение рассылки", color = Color.Black, fontSize = 15.sp)
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

