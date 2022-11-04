package com.example.gramclient.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.gramclient.R
import com.example.gramclient.RoutesName
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomBar(
    navController: NavHostController,
    mainBottomSheetState: BottomSheetScaffoldState,
    bottomSheetState: BottomSheetScaffoldState,
) {
    val coroutineScope= rememberCoroutineScope()
    BottomAppBar(
        backgroundColor = Color(0xFFF7F7F7),
        contentColor = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 0.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            IconButton(onClick = {
                coroutineScope.launch {
                    if(mainBottomSheetState.bottomSheetState.isExpanded){
                        mainBottomSheetState.bottomSheetState.collapse()
                    } else{
                        mainBottomSheetState.bottomSheetState.expand()
                    }
                }
            }) {
                Image(
                    modifier = Modifier.size(30.dp),
                    imageVector = ImageVector.vectorResource(if(mainBottomSheetState.bottomSheetState.isCollapsed) R.drawable.options_icon else R.drawable.arrow_down),
                    contentDescription = "icon"
                )
            }
            Button(
                onClick = {
                    navController.navigate(RoutesName.MAIN_SCREEN)
                },
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Black)
                    .width(260.dp)
                    .height(54.dp)
                    .padding(top = 0.dp),
                enabled =  true ,
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF2264D1), contentColor = Color.White),
                content = { Text(text = "Заказать", fontWeight = FontWeight.Bold, fontSize = 18.sp, lineHeight = 28.sp) },
            )
            IconButton(onClick = {
                coroutineScope.launch {
                    if(bottomSheetState.bottomSheetState.isCollapsed){
                       bottomSheetState.bottomSheetState.expand()
                    } else{
                        bottomSheetState.bottomSheetState.collapse()
                    }
                }
            }) {
                Image(
                    modifier = Modifier.size(30.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.cash_icon),
                    contentDescription = "icon"
                )
            }
        }
    }
}