package com.gram.client.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import com.gram.client.domain.database.usecase.RoomUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val getGroupsUseCase: RoomUseCase
) : ViewModel() {
    fun getTitle() {
        Log.d(
            "sadsa", "Get"
        )
        Log.d(
            "sadsa", ""+getGroupsUseCase.invoke()[0].name
        )
        Log.d(
            "sadsa", ""+getGroupsUseCase.getStories(1)[0].title
        )
    }
}