package com.example.gramclient.presentation.screens.main.components

import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
        modifier = Modifier
            .size(50.dp)
            .offset(y = if (bottomSheetState.bottomSheetState.isCollapsed) (-35).dp else (-65).dp),
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