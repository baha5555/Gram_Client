package com.gram.client.presentation.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil.compose.rememberAsyncImagePainter
import com.gram.client.R
import com.gram.client.app.preference.CustomPreference
import com.gram.client.presentation.MainActivity
import com.gram.client.presentation.screens.authorization.AuthScreen
import com.gram.client.presentation.screens.drawer.AboutScreen
import com.gram.client.presentation.screens.drawer.orderHistoryScreen.OrdersHistoryScreen
import com.gram.client.presentation.screens.drawer.setting_screens.SettingScreen
import com.gram.client.presentation.screens.main.SearchAddressScreen
import com.gram.client.presentation.screens.profile.ProfileScreen
import com.gram.client.presentation.screens.promocod.PromocodScreen
import com.gram.client.utils.Constants
import com.gram.client.utils.Values
import kotlinx.coroutines.launch

@Composable
fun SideBarMenu() {
    val isDialogOpen = remember { mutableStateOf(false) }
    val prefs = CustomPreference(LocalContext.current)
    val navigator = LocalNavigator.currentOrThrow

    val currentScreen = navigator.lastItem.key
    val coroutineScope = rememberCoroutineScope()
    //val currentScreen = navController.currentBackStackEntryAsState().value?.destination?.route ?: ""
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(color = colorResource(id = R.color.primary_color))
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.padding(20.dp)
        ) {
            Box {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = if (Values.ImageUrl.value != "") Values.ImageUrl.value
                            ?: R.drawable.camera_plus_light else R.drawable.camera_plus_light
                    ),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .padding(if (Values.ImageUrl.value == "") 5.dp else 0.dp),
                    contentDescription = "",
                )
            }
            Column(Modifier.padding(start = 15.dp)) {
                Text(
                    modifier = Modifier.clickable {
                        coroutineScope.launch {
                            if (prefs.getAccessToken() == "") navigator.plusAssign(AuthScreen())
                            else navigator.push(ProfileScreen())
                        }
                    },
                    text = if (Values.FirstName.value != "" && Values.LastName.value != "" && Values.FirstName.value != null && Values.LastName.value != null) Values.FirstName.value + " " + Values.LastName.value else "Выбрать Имя...",
                    fontSize = 22.sp,
                    color = Color.White
                )
                Text(
                    modifier = Modifier.clickable {
                        if (prefs.getAccessToken() == "") {
                            if (currentScreen == SearchAddressScreen().key)
                                Constants.IDENTIFY_TO_SCREEN = "SearchAddressScreen"
                            navigator.plusAssign(AuthScreen())
                        } else navigator.push(ProfileScreen())
                    },
                    text = if (Values.Email.value != "") Values.Email.value else "Выбрать Почту...",
                    fontSize = 18.sp,
                    color = Color.White
                )
            }
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
                ShowItems(iconList[i], textList[i], isDialogOpen)
            }
        }
        val activity = (LocalContext.current as MainActivity)
        CustomDialog(
            text = "Действительно выйти?",
            okBtnClick = {
                activity.finish()
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
    isDialogOpen: MutableState<Boolean>
) {
    val context = LocalContext.current
    val prefs = CustomPreference(context)
    val navigator = LocalNavigator.currentOrThrow
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                when (text) {
                    "Параметры" -> {
                        devToast(context)
/*
                        navigator.push(SettingScreen())
*/
                    }
                    "Мои адреса" -> devToast(context)
                    /*navController.navigate(RoutesName.MY_ADDRESSES_SCREEN)*/
                    "Поддержка" -> devToast(context)
                    /*navController.navigate(RoutesName.SUPPORT_SCREEN)*/
                    "О приложении" -> navigator.push(AboutScreen())
                    "Выход" -> {
                        isDialogOpen.value = true
                    }
                    "История заказов" -> {
                        if (prefs.getAccessToken() == "") navigator.plusAssign(AuthScreen())
                        else navigator.push(OrdersHistoryScreen())
                    }
                    "Промокоды" -> {
                        devToast(context)
                       /* if (prefs.getAccessToken() == "") navigator.plusAssign(AuthScreen())
                        else navigator.push(PromocodScreen())*/
                    }
                    "Позвонить оператору" -> {
                        val callIntent: Intent = Uri
                            .parse("tel:0220")
                            .let { number ->
                                Intent(Intent.ACTION_DIAL, number)
                            }
                        context.startActivity(callIntent)
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

fun devToast(context: Context) {
    Toast
        .makeText(
            context,
            "Эта страница на стадии разработки",
            Toast.LENGTH_LONG
        )
        .show()
}