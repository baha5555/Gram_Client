package com.example.gramclient.presentation.screens.main.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.gramclient.presentation.screens.main.MainViewModel
import com.example.gramclient.presentation.screens.main.addressComponents.AddressList
import com.example.gramclient.utils.Constants
import com.example.gramclient.utils.RoutesName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchResultContent(
    searchText: MutableState<String>,
    focusManager: FocusManager,
    isAddressList: MutableState<Boolean>,
    bottomSheetState: BottomSheetScaffoldState,
    isSearchState: MutableState<Boolean>,
    scope: CoroutineScope,
    mainViewModel: MainViewModel,
    WHICH_ADDRESS: MutableState<String>
) {
    //val currentRoute = navController.currentBackStackEntry?.destination?.route
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp, top = 10.dp)
    )
    {
        AddressList(
            isVisible = isAddressList,
            address = searchText,
            focusManager = focusManager,
        ) { address ->
            scope.launch {
                bottomSheetState.bottomSheetState.collapse()
            }
            isSearchState.value = false
            when (WHICH_ADDRESS.value) {
                Constants.FROM_ADDRESS -> {
                    mainViewModel.updateFromAddress(address)
                }
                Constants.TO_ADDRESS -> {
                    mainViewModel.updateToAddress(address)
                    /*if (currentRoute == RoutesName.SEARCH_ADDRESS_SCREEN) {
                        navController.navigate(RoutesName.MAIN_SCREEN)
                    }*/
                }
            }
        }
    }
}