package com.example.gramclient.presentation.components.voyager

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import com.example.gramclient.presentation.screens.main.MainViewModel
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable


class AddStopScreen : Screen {
    @Composable
    override fun Content() {
        val bottomNavigator = LocalBottomSheetNavigator.current
        val vm: MainViewModel = hiltViewModel()
        val state = rememberReorderableLazyListState(onMove = vm::move)
        val data = vm.toAddresses
        LazyColumn(
            state = state.listState,
            modifier = Modifier.reorderable(state)
        ) {
            items(data,{it.id}) { item ->
                ReorderableItem(state, item.id) { dragging ->
                    val elevation = animateDpAsState(if (dragging) 8.dp else 0.dp)
                        Column(
                            modifier = Modifier
                                .detectReorderAfterLongPress(state)
                                .shadow(elevation.value)
                                .fillMaxWidth()
                                .background(MaterialTheme.colors.surface)
                        ) {
                            Text(
                                text = item.address,
                                modifier = Modifier.padding(16.dp)
                            )
                            Divider()
                        }
                }
                /*Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .draggedItem(state.offsetByKey(item))
                        .detectReorderAfterLongPress(state)
                        .background(Color.Blue)
                ) {
                    Text(text = ""+item.address)
                }
                Row() {
//                    IconButton(onClick = {
//                        vm.removeAddStop(item)
//                        if(data.size==1){
//                            bottomNavigator.hide()
//                        }
//                    }) {
//                        Icon(imageVector = Icons.Default.Close, contentDescription = "")
//                    }
                }*/
            }
        }
    }
}