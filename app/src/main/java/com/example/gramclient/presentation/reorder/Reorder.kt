/*
package com.example.gramclient.presentation.reorder

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.draggedItem
import org.burnoutcrew.reorderable.rememberReorderState
import org.burnoutcrew.reorderable.reorderable

@Composable
fun Reorder() {
    val state = rememberReorderState()
    val vm: ReorderListViewModel = hiltViewModel()
    val data = vm::data.get()
    LazyColumn(
        state = state.listState,
        modifier = Modifier.reorderable(state, onMove = vm::move)
    ) {
        items(data, { it }) { item ->
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .draggedItem(state.offsetByKey(item))
                    .detectReorderAfterLongPress(state)
                    .background(Color.Blue)
            ) {
                Text(text = ""+item)
            }
        }
    }
}*/
