package com.gram.client.data.room.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gram.client.data.room.Dao.PostDao
import com.gram.client.data.room.entity.GroupEntity
import com.gram.client.data.room.entity.StoriesEntity

@Database(
    entities = [GroupEntity::class, StoriesEntity::class],
    version = 1,
    exportSchema = true
)
abstract class PostDatabase : RoomDatabase() {
    abstract  fun getPostDao(): PostDao
}