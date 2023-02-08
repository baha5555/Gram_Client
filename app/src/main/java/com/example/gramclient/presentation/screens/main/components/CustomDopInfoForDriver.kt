package com.example.gramclient.presentation.screens.main.components

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gramclient.presentation.components.CustomButton
import com.example.gramclient.presentation.screens.main.MainViewModel
import com.example.gramclient.ui.theme.BackgroundColor
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnrememberedMutableState")
@Composable
fun CustomDopInfoForDriver(
    onClick:()->Unit,
    textFieldValue:String,
    onValueChange:(String)->Unit,
    placeholder:String,
    title:String,
    stateTextField:Boolean = false,
    stateViewOfInfo:Boolean = false,
    planTripTenMinutesOnClick:()->Unit = {},
    planTripFifteenMinutesOnClick:()->Unit = {},
    selectedTime:MutableState<String> = mutableStateOf(""),
    selectedDate:MutableState<String> = mutableStateOf(""),
) {
    var stateTimeDialog by remember{mutableStateOf(false)}
    var stateDatePickerDialog by remember{mutableStateOf(false)}
    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(if (!stateViewOfInfo) 0.9f else 0.45f)) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .background(BackgroundColor)
            .padding(vertical = 20.dp), contentAlignment = Alignment.Center) {
            Text(
                text = title,
                fontSize = 18.sp,
                modifier = Modifier.padding(end = 10.dp)
            )
        }
        if(!stateViewOfInfo) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .padding(start = 10.dp, top = 10.dp),
                value = textFieldValue, onValueChange = { onValueChange(it) },
                placeholder = { Text(text = placeholder) },
                singleLine = stateTextField,
                shape = RoundedCornerShape(15.dp),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    cursorColor = Color.Black,
                    leadingIconColor = Color.Black,
                    trailingIconColor = Color.Black,
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = if (stateTextField) Color.Black else Color.Transparent,
                    unfocusedIndicatorColor = if (stateTextField) Color.Black else Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = if (stateTextField) KeyboardType.Number else KeyboardType.Text),
            )
        }
        else {
            Text(text = "Через 10 минут", modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 20.dp)
                .clickable { planTripTenMinutesOnClick() })
            Text(text = "Через 15 минут", modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 20.dp)
                .clickable { planTripFifteenMinutesOnClick() })
            Divider(modifier = Modifier.padding(horizontal = 10.dp))
            DatePicker(value = selectedDate.value, onValueChange = {
                selectedDate.value = it
                if(it!="")
                {
                    stateDatePickerDialog =false
                    stateTimeDialog = true
                }
            }, stateDatePickerDialog = stateDatePickerDialog)
            CustomTimePicker(value = selectedTime.value, onValueChange = {
                selectedTime.value = it
                if(it!="")
                {
                    stateTimeDialog =false
                }
            }, stateTimeDialog = stateTimeDialog)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 20.dp)
                    .clickable {
                               stateDatePickerDialog = true
                },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(text = if(selectedDate.value=="" && selectedTime.value=="")"Указать дату и время" else "${selectedDate.value} ${selectedTime.value}")
                Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null)
            }
        }
        if(textFieldValue!="" && !stateViewOfInfo) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                CustomButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 10.dp)
                        .height(50.dp),
                    text = "Сохранить", textSize = 16, onClick = onClick,
                    textBold = false
                )
            }
        }
        else if(stateViewOfInfo){
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                CustomButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 10.dp)
                        .height(50.dp),
                    text = "Сохранить", textSize = 16, onClick = onClick,
                    textBold = false
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CustomTimePicker(
    value: String,
    onValueChange: (String) -> Unit,
    pattern: String = "HH:mm",
    is24HourView: Boolean = true,
    stateTimeDialog:Boolean
) {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    val time = if (value.isNotBlank()) LocalTime.parse(value, formatter) else LocalTime.now()
    val dialog = TimePickerDialog(
        LocalContext.current,
        { _, hour, minute -> onValueChange(LocalTime.of(hour, minute).toString()) },
        time.hour,
        time.minute,
        is24HourView,
    )
    if(stateTimeDialog)
        dialog.show()
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePicker(
    value: String,
    onValueChange: (String) -> Unit = {},
    pattern: String = "yyyy-MM-dd",
    stateDatePickerDialog: Boolean
) {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    val sts = ""
    val date = if (value.isNotBlank()) LocalDate.parse(value, formatter) else LocalDate.now()
    val dialog = DatePickerDialog(
        LocalContext.current,
        { _, year, month, dayOfMonth ->
            onValueChange(LocalDate.of(year, month + 1, dayOfMonth).toString())
        },
        date.year,
        date.monthValue - 1,
        date.dayOfMonth,
    )
    if(stateDatePickerDialog)
        dialog.show()
}


