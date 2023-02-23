package com.example.gramclient.presentation.components.voyager

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.gramclient.R
import com.example.gramclient.domain.mainScreen.Address
import com.example.gramclient.presentation.screens.main.MainViewModel
import com.example.gramclient.presentation.screens.main.components.FloatingButton
import com.example.gramclient.presentation.screens.map.CustomMainMap
import com.example.gramclient.presentation.screens.map.mLocationOverlay
import com.example.gramclient.presentation.screens.map.map
import com.example.gramclient.ui.theme.PrimaryColor
import com.example.gramclient.utils.Values

class MapPointScreen : Screen {
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val mainViewModel: MainViewModel = hiltViewModel()
        val navigator = LocalNavigator.currentOrThrow
        BottomSheetScaffold(
            sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            sheetContent = {
                SheetContent{
                    val address: Address = Address("Мекта на карте")
                    mainViewModel.updateFromAddress(address)
                    navigator.pop()
                }
            },
            sheetPeekHeight = 150.dp,
            floatingActionButton = {

                if (Values.ClientOrders.value != null) {
                    Box(modifier = Modifier.offset(25.dp, (-55).dp)) {
                        FloatingButton(
                            Icons.Filled.ArrowBack,
                            backgroundColor = MaterialTheme.colors.background,
                            contentColor = MaterialTheme.colors.onBackground
                        ) {
                            //navigator.replaceAll(SearchDriverScreen())
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 25.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    FloatingButton(
                        Icons.Filled.ArrowBack,
                        backgroundColor = Color.White,
                        contentColor = PrimaryColor
                    ) {
                        navigator.pop()
                    }
                    FloatingButton(
                        ImageVector.vectorResource(id = R.drawable.btn_show_location)
                    ) {
                        map.controller.animateTo(mLocationOverlay.myLocation)
                    }
                }
            }) {
            CustomMainMap(mainViewModel = mainViewModel)
        }
    }

    @Composable
    fun SheetContent(onClick: () -> Unit) {
        Column(
            modifier = Modifier
                .height(150.dp)
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column() {
                Text(
                    text = "Точка отправления",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium
                )
                Divider(modifier = Modifier.padding(vertical = 10.dp))
            }
//                    Row(verticalAlignment = Alignment.CenterVertically) {
//                        Image(imageVector = ImageVector.vectorResource(id = R.drawable.from_marker), contentDescription = "")
//                        Text(text = "Метка на карте", fontSize = 18.sp, modifier = Modifier.padding(start = 10.dp))
//                    }
            Button(
                onClick = {
                    onClick.invoke()
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(text = "Готово", fontSize = 20.sp)
            }
        }
    }

}