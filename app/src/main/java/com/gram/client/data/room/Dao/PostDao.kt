package com.gram.client.data.room.Dao

import androidx.room.Dao
import androidx.room.Query
import com.gram.client.data.room.entity.GroupEntity
import com.gram.client.data.room.entity.StoriesEntity

@Dao
interface PostDao {

    @Query("Select * From `stories` WHERE id_group=:id_group ")
    fun getStories(id_group: Int): List<StoriesEntity>

    @Query("Select * From `group_stories`")
    fun getStoriesGroup(): List<GroupEntity>

}