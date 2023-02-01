package com.example.gramclient.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.gramclient.presentation.components.CustomTopBar
import com.example.gramclient.ui.theme.BackgroundColor
import com.example.gramclient.ui.theme.PrimaryColor
import com.example.gramclient.utils.Constants.KONFIG_URL

@Composable
fun AboutScreen(navController: NavHostController) {
    val mAnnotatedLinkString = buildAnnotatedString {
        addStringAnnotation(
            tag = "URL",
            annotation = KONFIG_URL,
            start = 0,
            end = 0
        )
    }
    val mUriHandler = LocalUriHandler.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        CustomTopBar(title = "О приложении", navController = navController)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 15.dp, horizontal = 30.dp)
        ) {
            Text(
                text = "Gram - заказ такси",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 24.sp
            )
            Text(text = "версия 1.1", fontSize = 20.sp)
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = "Приложение предназначено для создания заказа автотранспорта, услуг и информирования заказчика о его исполнении.",
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = "Созданный заказ посредством приложения передается партнерам сервиса для последующего исполнения.",
                fontSize = 20.sp
            )
            Spacer(
                modifier = Modifier
                    .height(30.dp)
            )
            Text(
                modifier = Modifier.clickable {
                    mAnnotatedLinkString
                        .getStringAnnotations("URL", 0, 0)
                        .firstOrNull()
                        ?.let { stringAnnotation ->
                            mUriHandler.openUri(stringAnnotation.item)
                        }
                },
                text = "Условия оказания услуг",

                textDecoration = TextDecoration.Underline,
                color = PrimaryColor,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(30.dp)
            )
            Text(
                modifier = Modifier.clickable {
                    mAnnotatedLinkString
                        .getStringAnnotations("URL", 0, 0)
                        .firstOrNull()
                        ?.let { stringAnnotation ->
                            mUriHandler.openUri(stringAnnotation.item)
                        }
                },
                text = "Политика конфиденциальности",
                textDecoration = TextDecoration.Underline,
                color = PrimaryColor,
                fontSize = 20.sp
            )

        }
    }
}

