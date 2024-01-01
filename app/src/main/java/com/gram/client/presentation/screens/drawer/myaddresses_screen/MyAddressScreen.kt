package com.gram.client.presentation.screens.drawer.myaddresses_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
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
import com.gram.client.utils.getAddressText

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
                            getAddressText(it.address)
                        ){
                            navigator.push(EditAddressScreen(it.name, it.address, it.meet_info, it.comment_to_driver, it.type, it.id))
                        }
                    }

                }else{
                    ListAddressesShow(R.drawable.ic_add, "Добавить адрес дома", null){
                        navigator.push(AddAddressScreen("home"))
                    }
                }
                if((stateMyAddresses.response?.result?.work ?: "") != ""){
                    stateMyAddresses.response?.result?.work?.get(0)?.let {
                        ListAddressesShow(
                            R.drawable.ic_work, "Работа",
                            getAddressText(it.address)
                        ) {
                            navigator.push(EditAddressScreen(it.name, it.address, it.meet_info, it.comment_to_driver, it.type, it.id))
                        }
                    }

                }else{
                    ListAddressesShow(R.drawable.ic_add, "Добавить адрес работы", null){
                        navigator.push(AddAddressScreen("work"))
                    }
                }
                stateMyAddresses.response?.result?.other?.forEach {
                    ListAddressesShow(R.drawable.ic_favorites, it.name,getAddressText(it.address)
                    ) {
                        navigator.push(EditAddressScreen(it.name, it.address, it.meet_info, it.comment_to_driver, it.type, it.id))
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
    }.padding(vertical = 0.dp, horizontal = 10.dp)) {
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
            Text(text=title, fontSize = 16.sp, fontWeight = FontWeight.Medium, maxLines = 1)
            if (text != null) {
                Text(text=text, fontSize = 12.sp, maxLines = 1)
            }
        }
    }
}
