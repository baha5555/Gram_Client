package com.example.gramclient.presentation.myaddresses_screen

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import com.example.gramclient.presentation.components.CustomSearch
import com.example.gramclient.presentation.components.CustomTopBar

@Composable
fun AddAddresScreen(navController: NavHostController){
    Scaffold(topBar = { CustomTopBar(title = "Добавление адреса", navController = navController) }) {
        val search = remember {
            mutableStateOf("")
        }
        CustomSearch(search = search, "Название, например “Дом”")
    }
}