package com.gram.client.presentation.components.voyager

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import com.gram.client.presentation.screens.main.MainViewModel
import com.gram.client.utils.Constants
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable


class AddStopScreen(val function: () -> Unit) : Screen {
    @SuppressLint("SuspiciousIndentation")
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
            itemsIndexed(items = data, key = { _, it -> it.idIncrement }) { inx, item ->
                ReorderableItem(state, item.idIncrement) { dragging ->
                    val elevation = animateDpAsState(if (dragging) 8.dp else 0.dp)
                    Column(
                        modifier = Modifier
                            .detectReorderAfterLongPress(state)
                            .shadow(elevation.value)
                            .fillMaxWidth()
                            .background(MaterialTheme.colors.surface)
                            .clickable {
                                bottomNavigator.show(SearchAddressNavigator(
                                    Constants.TO_ADDRESS,
                                    inx
                                ) {

                                }
                                )
                            }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth(0.8f)
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .padding(start = 10.dp)
                                        .padding(vertical = 10.dp)
                                        .size(30.dp)
                                        .border(
                                            1.dp,
                                            MaterialTheme.colors.onBackground,
                                            RoundedCornerShape(100)
                                        )
                                ) {
                                    Text(text = "" + (inx + 1), fontSize = 18.sp)
                                }
                                Text(
                                    text = item.name,
                                    modifier = Modifier.padding(start = 10.dp),
                                    maxLines = 2
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Outlined.Close,
                                    contentDescription = "",
                                    tint = Color.Red,
                                    modifier = Modifier
                                        .size(30.dp)
                                        .padding(end = 5.dp)
                                        .clickable {
                                            vm.removeAddStop(item)
                                            if (data.size == 1) {
                                                bottomNavigator.hide()
                                            }
                                        }
                                )
                                Icon(
                                    imageVector = Icons.Outlined.Menu,
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(35.dp)
                                        .padding(end = 10.dp)
                                )
                            }
                        }
                        Divider()
                    }
                }
            }
            item {
                Button(
                    onClick = {
                        bottomNavigator.show(SearchAddressNavigator(
                            Constants.ADD_TO_ADDRESS,
                            -2
                        ) {
                            function.invoke()
                        }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Добавить остановку", color = Color.White, fontSize = 16.sp)
                }
            }
        }
    }
}