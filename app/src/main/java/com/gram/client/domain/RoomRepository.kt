package com.gram.client.domain

import com.gram.client.domain.database.models.GroupModel
import com.gram.client.domain.database.models.StoriesModel

interface RoomRepository {
    fun getStoriesGroup(): List<GroupModel>
    fun getStories(id_group: Int): List<StoriesModel>
}