package com.example.gramclient.presentation.screens.order.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.gramclient.presentation.screens.main.MainViewModel
import com.example.gramclient.presentation.screens.main.addressComponents.AddressList
import com.example.gramclient.presentation.screens.order.OrderExecutionViewModel
import com.example.gramclient.ui.theme.BackgroundColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun searchSection(
    searchText: MutableState<String>,
    focusRequester: FocusRequester,
    isSearchState: MutableState<Boolean>,
    bottomSheetState: BottomSheetScaffoldState,
    scope: CoroutineScope,
    orderExecutionViewModel: OrderExecutionViewModel,
    isAddressList: MutableState<Boolean>,
    focusManager: FocusManager,
    mainViewModel: MainViewModel,
){
    Box(modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = searchText.value,
            onValueChange = { value ->
                searchText.value = value
                mainViewModel.searchAddress(value)
            },
            modifier = Modifier
                .focusRequester(focusRequester)
                .fillMaxWidth(),
            textStyle = TextStyle(color = Color.Black, fontSize = 18.sp),
            leadingIcon = {
                IconButton(onClick = { /*navController.popBackStack()*/ }) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(start = 15.dp, top = 15.dp, bottom = 15.dp)
                            .size(24.dp)
                    )
                }
            },
            trailingIcon = {
                if (searchText.value != "") {
                    IconButton(
                        modifier = Modifier.offset(x = (-40).dp),
                        onClick = {
                            searchText.value =
                                ""
                        }
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "",
                            modifier = Modifier
                                .padding(vertical = 15.dp, horizontal = 40.dp)
                                .size(24.dp)
                        )
                    }
                }
            },
            placeholder = { Text(text = "Введите адрес для поиска",modifier=Modifier.fillMaxWidth(0.9f)) },
            singleLine = true,
            shape = RoundedCornerShape(15.dp),
            colors = TextFieldDefaults.textFieldColors(
                textColor = MaterialTheme.colors.onSecondary,
                cursorColor = MaterialTheme.colors.onSecondary,
                leadingIconColor = Color.Black,
                trailingIconColor = Color.Black,
                backgroundColor = MaterialTheme.colors.onSecondary,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
        Box(modifier = Modifier
            .align(Alignment.CenterEnd)
            .padding(end = 20.dp)
            .clickable {
                scope.launch {
                    bottomSheetState.bottomSheetState.collapse()
                }
                isSearchState.value = false
            },
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically){
                Spacer(
                    modifier =
                    Modifier
                        .width(1.dp)
                        .height(55.dp)
                        .padding(vertical = 10.dp)
                        .background(Color(0xFFE0DBDB))
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "Карта", fontSize = 14.sp, color = Color.Black, modifier = Modifier)
            }
        }
    }
    //Search response content
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 10.dp, end = 10.dp, top = 10.dp)
    )
    {
        AddressList(
            isVisible = isAddressList,
            address = searchText,
            focusManager = focusManager,
        ){address ->
            scope.launch {
                bottomSheetState.bottomSheetState.collapse()
            }
            isSearchState.value=false
            //orderExecutionViewModel.editOrder(address.id)
            searchText.value = ""
        }
    }
}