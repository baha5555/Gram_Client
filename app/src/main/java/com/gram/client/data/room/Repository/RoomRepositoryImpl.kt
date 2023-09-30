package com.gram.client.data.room.Repository

import com.gram.client.data.room.Dao.PostDao
import com.gram.client.data.room.entity.toDomain
import com.gram.client.domain.RoomRepository
import com.gram.client.domain.database.models.GroupModel
import com.gram.client.domain.database.models.StoriesModel

class RoomRepositoryImpl(private val dao: PostDao) : RoomRepository {


    override fun getStoriesGroup(): List<GroupModel> {
        return dao.getStoriesGroup().map { it.toDomain() }
    }
    override fun getStories(id_group: Int): List<StoriesModel> {
        return dao.getStories(id_group).map { it.toDomain() }
    }


}