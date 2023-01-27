package com.example.gramclient.presentation.components

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.rememberAsyncImagePainter
import com.example.gramclient.utils.PreferencesName
import com.example.gramclient.R
import com.example.gramclient.app.preference.CustomPreference
import com.example.gramclient.presentation.MainActivity
import com.example.gramclient.utils.RoutesName
import com.example.gramclient.presentation.screens.profile.ProfileViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun SideBarMenu(
    navController: NavHostController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val isDialogOpen = remember { mutableStateOf(false) }
    val stateGetProfileInfo by viewModel.stateGetProfileInfo
    val prefs = CustomPreference(LocalContext.current)

    val coroutineScope = rememberCoroutineScope()
    val currentScreen = navController.currentBackStackEntryAsState().value?.destination?.route ?: ""
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
                        model = stateGetProfileInfo.response?.avatar_url ?: R.drawable.avatar
                    ),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape),
                    contentDescription = "",
                )
            }
            Column(Modifier.padding(start = 15.dp)) {
                Text(
                    modifier = Modifier.clickable {
                        coroutineScope.launch {
                            if (prefs.getAccessToken() == "") {
                                if (currentScreen == RoutesName.SEARCH_ADDRESS_SCREEN) {
                                    navController.navigate(RoutesName.AUTH_SCREEN) {
                                        popUpTo(RoutesName.SEARCH_ADDRESS_SCREEN) {
                                            inclusive = true
                                        }
                                    }
                                }
                                if (currentScreen == RoutesName.SEARCH_DRIVER_SCREEN) {
                                    navController.navigate(RoutesName.AUTH_SCREEN) {
                                        popUpTo(RoutesName.SEARCH_DRIVER_SCREEN) {
                                            inclusive = true
                                        }
                                    }
                                }
                            } else {
                                navController.navigate(RoutesName.PROFILE_SCREEN)
                            }
                        }
                    },
                    text = if (stateGetProfileInfo.response?.first_name != null && stateGetProfileInfo.response?.last_name != null) stateGetProfileInfo.response?.first_name + ' ' + stateGetProfileInfo.response?.last_name else "Выбрать Имя...",
                    fontSize = 22.sp,
                    color = Color.White
                )
                Text(
                    modifier = Modifier.clickable {
                        navController.navigate(RoutesName.PROFILE_SCREEN)
                    },
                    text = if (stateGetProfileInfo.response?.email != null) stateGetProfileInfo.response!!.email else "Выбрать Почту...",
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
                ShowItems(iconList[i], textList[i], navController, isDialogOpen)
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
//        CustomDialog(
//            text = "Вы уверены что хотите выйти?",
//            okBtnClick = {
//                isDialogOpen.value = false
//                prefs.setAccessToken("")
//                navController.navigate(RoutesName.AUTH_SCREEN)
//            },
//            cancelBtnClick = { isDialogOpen.value = false },
//            isDialogOpen = isDialogOpen.value
//        )
}

@Composable
fun ShowItems(
    icon: ImageVector,
    text: String,
    navController: NavHostController,
    isDialogOpen: MutableState<Boolean>
) {
    val context = LocalContext.current
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
                    "Позвонить оператору" -> {
                        val callIntent: Intent = Uri
                            .parse("tel:0666")
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