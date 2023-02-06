package com.example.gramclient.presentation.screens.main.components

import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.gramclient.presentation.screens.main.SearchAddressScreen
import com.example.gramclient.presentation.screens.order.SearchDriverScreen
import com.example.gramclient.ui.theme.PrimaryColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FloatingButton(
    scope: CoroutineScope,
    drawerState: DrawerState,
    bottomSheetState: BottomSheetScaffoldState
) {
    FloatingActionButton(

        backgroundColor = PrimaryColor,
        onClick = {
            scope.launch {
                drawerState.open()
            }
        }
    ) {
        Icon(
            Icons.Filled.Menu,
            contentDescription = "Menu", tint = Color.White,
            modifier = Modifier.size(25.dp)
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FloatingButton1(
    scope: CoroutineScope,
    drawerState: DrawerState,
    bottomSheetState: BottomSheetScaffoldState
) {
    val navigator = LocalNavigator.currentOrThrow
    val coroutineScope = rememberCoroutineScope()

    FloatingActionButton(


        backgroundColor = Color.White,
        onClick = {
            coroutineScope.launch {
                navigator.replaceAll(SearchDriverScreen())
            }
        }
    ) {
        Icon(
            Icons.Filled.ArrowBack,
            contentDescription = "Menu", tint = Color.Black,
            modifier = Modifier.size(25.dp)
        )
    }
}