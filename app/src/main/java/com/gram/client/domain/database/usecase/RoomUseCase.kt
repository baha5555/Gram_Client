package com.gram.client.domain.database.usecase

import com.gram.client.domain.RoomRepository
import com.gram.client.domain.database.models.GroupModel
import com.gram.client.domain.database.models.StoriesModel
import javax.inject.Inject

class RoomUseCase @Inject constructor(private val roomRepository: RoomRepository) {
    operator fun invoke(): List<GroupModel> {
        return roomRepository.getStoriesGroup()
    }
    fun getStories(id_group: Int): List<StoriesModel> {
        return roomRepository.getStories(id_group)
    }
}