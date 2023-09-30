package com.gram.client.presentation.screens.story

import androidx.lifecycle.ViewModel
import com.gram.client.domain.database.models.GroupModel
import com.gram.client.domain.database.models.StoriesModel
import com.gram.client.domain.database.usecase.RoomUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StoryViewModel @Inject constructor(
    private val roomUseCase: RoomUseCase
) : ViewModel() {
    fun getStoriesGroup(): List<GroupModel> {
        return roomUseCase.invoke()
    }
    fun getStories(id_group: Int): List<StoriesModel> {
        return roomUseCase.getStories(id_group)
    }
}