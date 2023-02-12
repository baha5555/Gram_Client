package com.example.gramclient.presentation.reorder

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.burnoutcrew.reorderable.ItemPosition
import javax.inject.Inject

@HiltViewModel
class ReorderListViewModel @Inject constructor() : ViewModel(){
    var data = List(3) { "item $it" }.toMutableStateList()
    fun move(from: ItemPosition, to: ItemPosition) {
        data = data.apply {
            add(to.index, removeAt(from.index))
        }
    }
}