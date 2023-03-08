package com.gram.client.presentation.screens.drawer.setting_screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import com.gram.client.presentation.components.CustomCheckBox

class DecorScreenItem() : Screen {
    @Composable
    override fun Content() {
        val reasonsCheck = remember { mutableStateOf("") }
        val bottomNavigator = LocalBottomSheetNavigator.current
        val item = listOf("Системное", "Темное", "Светлое")

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
        ) {
            Text(
                "Тема",
                fontSize = 28.sp,
                modifier = Modifier.padding(vertical = 10.dp),
                fontWeight = FontWeight.Medium
            )
            item.forEach {
                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { reasonsCheck.value = it }
                        .padding(vertical = 10.dp)) {
                    Text(it, fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    CustomCheckBox(
                        isChecked = reasonsCheck.value == it,
                        onChecked = { reasonsCheck.value = it })
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    bottomNavigator.hide()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(57.dp),
                shape = RoundedCornerShape(15.dp),
                elevation = ButtonDefaults.elevation(0.dp)
            ) {
                Text(text = "Готово", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            }

        }
    }
}