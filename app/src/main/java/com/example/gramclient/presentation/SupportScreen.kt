package com.example.gramclient.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.gramclient.R
import com.example.gramclient.RoutesName
import com.example.gramclient.presentation.components.CustomButton
import com.example.gramclient.presentation.components.CustomTopBar
import com.example.gramclient.ui.theme.BackgroundColor
import com.example.gramclient.ui.theme.PrimaryColor

@Composable
fun SupportScreen(navController: NavHostController){
    Column(
        modifier = Modifier.fillMaxSize()
    ){
        CustomTopBar(title = "Поддержка", navController = navController)
        Box(modifier = Modifier.fillMaxSize()){
            Column(modifier = Modifier.fillMaxSize()){
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.35f)
                        .background(Color(0xFF2264D1))
                        .padding(35.dp)
                ){
                    Text(text = "Gram", fontSize = 36.sp, lineHeight = 34.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start){
                        Text(text = "Здравствуйте!", fontSize = 24.sp, lineHeight = 34.sp, color = Color.White)
                        Image(
                            modifier = Modifier
                                .size(35.dp),
                            painter = painterResource(R.drawable.hand_icon),
                            contentDescription = "icon"
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Спросите нас о чем угодно или\n" +
                            "поделитесь своими отзывами.", fontSize = 18.sp, color = Color.White)
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .background(BackgroundColor)
                ){
                }
            }
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(top = 190.dp)
            , verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {
                Card(
                    modifier = Modifier
                        .width(349.dp),
                    elevation = 1.dp,
                    shape = RectangleShape
                ){
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(19.dp)
                    ){
                        Text(text = "Напишите нам", fontSize = 13.sp)
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start, modifier = Modifier.padding(top = 14.dp)){
                            Image(
                                modifier = Modifier
                                    .size(52.dp),
                                painter = painterResource(R.drawable.operator_icon),
                                contentDescription = "icon"
                            )
                            Spacer(modifier = Modifier.width(25.dp))
                            Column {
                                Text(text = "Среднее время ответа!", fontSize = 13.sp, color = Color.Gray)
                                Spacer(modifier = Modifier.height(7.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        modifier = Modifier
                                            .size(14.dp),
                                        tint = PrimaryColor,
                                        painter = painterResource(R.drawable.clock_icon),
                                        contentDescription = "icon",
                                    )
                                    Spacer(modifier = Modifier.width(7.dp))
                                    Text(text = "несколько минут", fontSize = 13.sp, color = Color.Gray)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(13.dp))
                        CustomButton(modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .fillMaxWidth()
                            .height(35.dp),
                            text = "Отправить сообщение", textSize = 14, textBold = false,
                        onClick = {
                            navController.navigate(RoutesName.MESSAGE_SCREEN)
                        })
                    }
                }

            }
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(top = 375.dp)
                , verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {
                Card(
                    modifier = Modifier
                        .width(349.dp),
                    elevation = 1.dp,
                    shape = RectangleShape
                ){
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(19.dp)
                    ){
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Что-то срочное?", fontSize = 13.sp, textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(15.dp))
                        CustomButton(modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .fillMaxWidth()
                            .height(35.dp),
                            text = "Позвоните", textSize = 14, textBold = false) {

                        }
                    }
                }

            }
        }
    }
}