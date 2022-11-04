package com.example.gramclient.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DrawerState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.gramclient.R
import com.example.gramclient.RoutesName

@Composable
fun SideBarMenu(drawerState: DrawerState, navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2264D1))
    ){
        TopAppBar(
            modifier = Modifier.height(80.dp),
            backgroundColor = Color.White
        ) {
            Row(modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 12.dp, start = 21.dp, end = 21.dp),
                verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.SpaceBetween){
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
                .fillMaxWidth()
                .padding(top = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
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
            Text(text = "Ваше имя...", fontSize = 22.sp, color = Color.White)
            Spacer(modifier = Modifier.height(15.dp))
            Text(text = "Добавьте почту...", fontSize = 18.sp, color = Color.White)
        }
        Spacer(modifier = Modifier.height(30.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 21.dp),
        ){
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp), verticalAlignment = Alignment.CenterVertically){
                Image(
                    modifier = Modifier
                        .size(20.dp),
                    imageVector = ImageVector.vectorResource(id = R.drawable.phone_icon),
                    contentDescription = "",
                )
                Spacer(modifier = Modifier.width(18.dp))
                Text(text = "Позвонить оператору", fontSize = 18.sp, color = Color.White)
            }
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp), verticalAlignment = Alignment.CenterVertically){
                Image(
                    modifier = Modifier
                        .size(20.dp),
                    imageVector = ImageVector.vectorResource(id = R.drawable.star_icon),
                    contentDescription = "",
                )
                Spacer(modifier = Modifier.width(18.dp))
                Text(text = "Мои адреса", fontSize = 18.sp, color = Color.White)
            }
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp), verticalAlignment = Alignment.CenterVertically){
                Image(
                    modifier = Modifier
                        .size(20.dp),
                    imageVector = ImageVector.vectorResource(id = R.drawable.clock_icon),
                    contentDescription = "",
                )
                Spacer(modifier = Modifier.width(18.dp))
                Text(text = "История заказов", fontSize = 18.sp, color = Color.White)
            }
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp), verticalAlignment = Alignment.CenterVertically){
                Image(
                    modifier = Modifier
                        .size(20.dp),
                    imageVector = ImageVector.vectorResource(id = R.drawable.promocode_icon),
                    contentDescription = "",
                )
                Spacer(modifier = Modifier.width(18.dp))
                Text(text = "Промокоды", fontSize = 18.sp, color = Color.White)
            }
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp), verticalAlignment = Alignment.CenterVertically){
                Image(
                    modifier = Modifier
                        .size(20.dp),
                    imageVector = ImageVector.vectorResource(id = R.drawable.message_icon),
                    contentDescription = "",
                )
                Spacer(modifier = Modifier.width(18.dp))
                Text(text = "Поддержка", fontSize = 18.sp, color = Color.White)
            }
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp).clickable { navController.navigate(
                RoutesName.SETTING_SCREEN) }, verticalAlignment = Alignment.CenterVertically){
                Image(
                    modifier = Modifier
                        .size(20.dp),
                    imageVector = ImageVector.vectorResource(id = R.drawable.parametres_icon),
                    contentDescription = "",
                )
                Spacer(modifier = Modifier.width(18.dp))
                Text(text = "Параметры", fontSize = 18.sp, color = Color.White)
            }
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp), verticalAlignment = Alignment.CenterVertically){
                Image(
                    modifier = Modifier
                        .size(20.dp),
                    imageVector = ImageVector.vectorResource(id = R.drawable.about_icon),
                    contentDescription = "",
                )
                Spacer(modifier = Modifier.width(18.dp))
                Text(text = "О приложении", fontSize = 18.sp, color = Color.White)
            }
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp), verticalAlignment = Alignment.CenterVertically){
                Image(
                    modifier = Modifier
                        .size(20.dp),
                    imageVector = ImageVector.vectorResource(id = R.drawable.logout_icon),
                    contentDescription = "",
                )
                Spacer(modifier = Modifier.width(18.dp))
                Text(text = "Выход", fontSize = 18.sp, color = Color.White)
            }
        }
    }
}