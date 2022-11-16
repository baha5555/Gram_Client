package com.example.gramclient.presentation.components

import android.content.SharedPreferences
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.gramclient.PreferencesName
import com.example.gramclient.R
import com.example.gramclient.RoutesName

@Composable
fun SideBarMenu(
    navController: NavHostController,
    preferences: SharedPreferences
) {
    val isDialogOpen = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2264D1))
    ) {
        TopAppBar(
            modifier = Modifier.height(80.dp),
            backgroundColor = Color.White
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 12.dp, start = 21.dp, end = 21.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    modifier = Modifier
                        .width(91.dp)
                        .height(28.dp),
                    imageVector = ImageVector.vectorResource(id = R.drawable.logo_gram_black),
                    contentDescription = "",
                )
                Text(text = "Худжанд", fontSize = 18.sp, color = Color.Black)
            }
        }
        Column(
            modifier = Modifier
                .clickable {
                    navController.navigate(RoutesName.PROFILE_SCREEN)
                }
                .fillMaxWidth()
                .padding(top = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .background(Color.White, shape = RoundedCornerShape(50.dp))
                    .padding(15.dp),
                content = {
                    Image(
                        modifier = Modifier
                            .width(60.dp)
                            .height(60.dp),
                        imageVector = ImageVector.vectorResource(id = R.drawable.camera_plus),
                        contentDescription = "",
                    )
                }
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "Ваше имя...", fontSize = 22.sp, color = Color.White
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "Добавьте почту...", fontSize = 18.sp, color = Color.White
            )
            Spacer(modifier = Modifier.height(30.dp))
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            val iconList = arrayOf(
                ImageVector.vectorResource(id = R.drawable.phone_icon),
                ImageVector.vectorResource(id = R.drawable.star_icon),
                ImageVector.vectorResource(id = R.drawable.clock_icon),
                ImageVector.vectorResource(id = R.drawable.promocode_icon),
                ImageVector.vectorResource(id = R.drawable.message_icon),
                ImageVector.vectorResource(id = R.drawable.parametres_icon),
                ImageVector.vectorResource(id = R.drawable.about_icon),
                ImageVector.vectorResource(id = R.drawable.logout_icon),
            )
            val textList = arrayOf(
                "Позвонить оператору",
                "Мои адреса",
                "История заказов",
                "Промокоды",
                "Поддержка",
                "Параметры",
                "О приложении",
                "Выход",
            )
            for (i in iconList.indices) {
                ShowItems(iconList[i], textList[i], navController, isDialogOpen)
            }
        }
        CustomDialog(
            text = "Вы уверены что хотите выйти?",
            okBtnClick = {
                isDialogOpen.value = false
                preferences.edit()
                    .putBoolean(PreferencesName.IS_AUTH, false)
                    .apply()
                navController.navigate(RoutesName.AUTH_SCREEN)
            },
            cancelBtnClick = { isDialogOpen.value = false },
            isDialogOpen = isDialogOpen.value
        )
    }
}

@Composable
fun ShowItems(
    icon: ImageVector,
    text: String,
    navController: NavHostController,
    isDialogOpen: MutableState<Boolean>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                when (text) {
                    "Параметры" -> navController.navigate(RoutesName.SETTING_SCREEN)
                    "Мои адреса" -> navController.navigate(RoutesName.MY_ADDRESSES_SCREEN)
                    "Поддержка" -> navController.navigate(RoutesName.SUPPORT_SCREEN)
                    "О приложении" -> navController.navigate(RoutesName.ABOUT_SCREEN)
                    "Выход" -> {
                        isDialogOpen.value = true
                    }
                    "История заказов" -> {
                        navController.navigate(RoutesName.ORDERS_HISTORY_SCREEN)
                    }
                    "Промокоды" -> {
                        navController.navigate(RoutesName.PROMO_CODE_SCREEN)
                    }
                }
            }
            .padding(vertical = 15.dp, horizontal = 21.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .size(20.dp),
            imageVector = icon,
            contentDescription = "",
        )
        Spacer(modifier = Modifier.width(18.dp))
        Text(text = text, fontSize = 18.sp, color = Color.White)
    }

}