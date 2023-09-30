package com.gram.client.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun RoomScreen(){
    val mainScreenViewModel: MainScreenViewModel = hiltViewModel()
    LaunchedEffect(key1 = true){
        mainScreenViewModel.getTitle()
    }
    Column {
        Text(text = "asdas")
    }
}