package com.gram.client.presentation.screens.drawer.myaddresses_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gram.client.R
import com.gram.client.presentation.components.CustomTopAppBar

class MyAddressScreen : Screen {
    @Composable
    override fun Content() {
        MyAddressesScreenContent()
    }

}
@Composable
fun MyAddressesScreenContent() {
    val myAddressViewModel: MyAddressViewModel = hiltViewModel()
    val stateMyAddresses = myAddressViewModel.stateGetAllMyAddresses.value
    LaunchedEffect(key1 = true){
        myAddressViewModel.getAllMyAddresses()
    }

    val navigator = LocalNavigator.currentOrThrow
    Scaffold(topBar = {
        CustomTopAppBar(title = "Мои адреса") { AddAddress() }
    }) {
        if(stateMyAddresses.isLoading){
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter){
                CircularProgressIndicator()
            }
        }
        else{
            Column(
                Modifier
                    .fillMaxSize()
                    .background(colorResource(id = R.color.white))
            ) {
                if((stateMyAddresses.response?.result?.home ?: "") != ""){
                    stateMyAddresses.response?.result?.home?.get(0)?.let {
                        ListAddressesShow(R.drawable.ic_home, "Дом",
                            it.name
                        ){
                            navigator.push(EditAddressScreen(it.name, it.search_address_id, it.meet_info, it.comment_to_driver, it.type, it.id))
                        }
                    }

                }else{
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navigator.push(AddAddressScreen("home"))
                        }) {
                        Image(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_add),
                            contentDescription = "back",
                            modifier = Modifier.padding(10.dp)
                        )
                        Text(text = "Добавить адрес дома", fontSize = 16.sp)
                    }
                }
                if((stateMyAddresses.response?.result?.work ?: "") != ""){
                    stateMyAddresses.response?.result?.work?.get(0)?.let {
                        ListAddressesShow(
                            R.drawable.ic_work, "Работа",
                            it.name
                        ) {
                            navigator.push(EditAddressScreen(it.name, it.search_address_id, it.meet_info, it.comment_to_driver, it.type, it.id))
                        }
                    }

                }else{
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navigator.push(AddAddressScreen("work"))
                        }) {
                        Image(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_add),
                            contentDescription = "back",
                            modifier = Modifier.padding(10.dp)
                        )
                        Text(text = "Добавить адрес работы", fontSize = 16.sp)
                    }
                }
                stateMyAddresses.response?.result?.other?.forEach {
                    ListAddressesShow(R.drawable.ic_favorites, it.name, it.name) {
                        navigator.push(EditAddressScreen(it.name, it.search_address_id, it.meet_info, it.comment_to_driver, it.type, it.id))
                    }
                }
            }
        }
    }

}

@Composable
fun AddAddress() {
    val navigator = LocalNavigator.currentOrThrow
    IconButton(onClick = {
       navigator.push(AddAddressScreen("other"))
    }) {
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_add),
            contentDescription = "back"
        )
    }
}


@Composable
fun ListAddressesShow(image: Int, title: String, text: String?, function: () -> Unit) {
    Row(Modifier.clickable {
        function.invoke()
    }.padding(vertical = 5.dp, horizontal = 10.dp)) {
        Box(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth(0.15f),
            contentAlignment = Alignment.Center
        ) {
            Image(
                imageVector = ImageVector.vectorResource(image),
                contentDescription = "home"
            )
        }
        Column(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text=title, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            if (text != null) {
                Text(text=text, fontSize = 12.sp)
            }
        }
    }
}
