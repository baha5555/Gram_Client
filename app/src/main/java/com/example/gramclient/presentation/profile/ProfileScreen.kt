package com.example.gramclient.presentation

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.gramclient.PreferencesName
import com.example.gramclient.presentation.components.CustomButton
import com.example.gramclient.presentation.components.CustomSwitch
import com.example.gramclient.presentation.components.CustomTopBar
import com.example.gramclient.presentation.profile.ProfileViewModel
import com.example.gramclient.ui.theme.BackgroundColor
import com.example.gramclient.ui.theme.FontSilver
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

@SuppressLint("UnrememberedMutableState", "RememberReturnType")
@ExperimentalCoilApi
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
    var selectImage by mutableStateOf<Uri?>(null)
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
            selectImage = it
        }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val photo = remember { mutableStateOf(File("$selectImage")) }

    viewModel.value.getProfileInfo(
        preferences.getString(PreferencesName.ACCESS_TOKEN, "").toString()
    )
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
                onClick = {
                    launcher.launch("image/*")

                }, modifier = Modifier
                    .padding(top = 21.dp)
                    .size(90.dp)
                    .background(Color.White, shape = RoundedCornerShape(50.dp))

            ) {
                if (selectImage != null) {
                    Image(
                        modifier = Modifier
                            .width(60.dp)
                            .height(60.dp),
                        painter = rememberImagePainter(selectImage),
//                    imageVector = ImageVector.vectorResource(id = R.drawable.camera_plus),
                        contentDescription = "",
                    )
                }
                Icon(imageVector = Icons.Default.Camera, contentDescription = null)
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
                    label = { Text(text = "Имя*") },
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
                    label = { Text(text = "Фамилия") },
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
                    label = { Text(text = "Email") },
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
                    onValueChange = { },
                    label = { Text(text = "Телефон") },
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
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
                            scope.launch {
                                try {
                                    Log.e("PHOTO", "IMAGE -> 1 ${photo.value.name}")
                                    val part = MultipartBody.Part
                                        .createFormData(
                                            name = "images/*",
                                            filename = photo.value.name,
                                            body = photo.value.asRequestBody()
                                        )

                                    viewModel.value.sendProfile(
                                        preferences.getString(PreferencesName.ACCESS_TOKEN, "")
                                            .toString(),
                                        profileFirstName.value!!,
                                        profileLastName.value!!,
                                        "0",
                                        "2022-01-01",
                                        profileEmail.value!!,
                                        part
                                    )
                                    Toast.makeText(
                                        context,
                                        "Фото успешно отправлено!",
                                        Toast.LENGTH_LONG
                                    ).show()
//                                    navController.popBackStack()
                                } catch (e: Exception) {
                                    Toast.makeText(
                                        context,
                                        "Произошла ошибка! Повторите еще раз",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }

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

