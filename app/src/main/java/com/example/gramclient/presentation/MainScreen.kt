package com.example.gramclient.presentation

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.gramclient.R
import com.example.gramclient.RoutesName
import com.example.gramclient.presentation.components.CustomButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen(navController: NavHostController){
    val bottomSheetScaffoldState =rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(BottomSheetValue.Collapsed)
    )
    val scope = rememberCoroutineScope()
    BottomSheetScaffold(
        sheetContent = {
            BottomSheetContent(navController)
        },
        scaffoldState = bottomSheetScaffoldState,
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        sheetBackgroundColor = Color.White,
         sheetPeekHeight = 240.dp,
        modifier =  Modifier
            .shadow(elevation = 28.dp, clip = false, shape = RectangleShape)
    ) {
        Scaffold(
            backgroundColor = Color(0xFFF8F6F6),
            topBar = { TopBar() },
        ) {
            ///
        }
    }
}


@Composable
fun TopBar() {
    TopAppBar(
        title = { Text(text = "GramClient", fontSize = 18.sp) },
        backgroundColor = Color.Blue,
        contentColor = Color.White
    )
}

@Composable
fun BottomSheetContent(navController: NavHostController) {
    val context = LocalContext.current
    var text by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier
                    .width(65.dp)
                    .height(7.dp),
                bitmap = ImageBitmap.imageResource(R.drawable.rectangle),
                contentDescription = "Logo"
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .width(35.dp)
                    .height(35.dp),
                bitmap = ImageBitmap.imageResource(R.drawable.from_marker),
                contentDescription = "Logo"
            )
            Spacer(modifier = Modifier.width(15.dp))
            TextField(
                placeholder = { Text("Откуда?") },
                value = text,
                onValueChange = {
                    text = it },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor =  Color.Gray,
                    unfocusedIndicatorColor = Color.Gray,
                    disabledIndicatorColor = Transparent
                )
            )
        }
        Spacer(modifier = Modifier.height(15.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .width(35.dp)
                    .height(35.dp),
                bitmap = ImageBitmap.imageResource(R.drawable.to_marker),
                contentDescription = "Logo"
            )
            Spacer(modifier = Modifier.width(15.dp))
            TextField(
                placeholder = { Text("Куда?") },
                value = text,
                onValueChange = {
                    text = it },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor =  Color.Gray,
                    unfocusedIndicatorColor = Color.Gray,
                    disabledIndicatorColor = Transparent
                )
            )
        }
        Spacer(modifier = Modifier.height(23.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    navController.navigate(RoutesName.SUBMITORDER_SCREEN)
                },
                modifier = Modifier
                    .clip(RoundedCornerShape(5.dp))
                    .background(Color.Black)
                    .fillMaxWidth()
                    .height(54.dp)
                    .padding(top = 0.dp),
                enabled =  true ,
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF3F51B5), contentColor = Color.White),
                content = { Text(text = "Подтвердить", fontWeight = FontWeight.Bold, fontSize = 18.sp, lineHeight = 28.sp) },
            )
        }
    }
}



