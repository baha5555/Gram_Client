package com.gram.client.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gram.client.domain.database.models.StoriesModel

@Entity(tableName = "stories")
data class StoriesEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id_stories")
    val id: Int,
    @ColumnInfo(name = "title")
    val title: String?,
    @ColumnInfo(name = "text")
    val text: String?,
    @ColumnInfo(name = "id_group")
    val id_group: Int?,
    @ColumnInfo(name = "img_src")
    val src: String?,
)

fun StoriesEntity.toDomain() = StoriesModel(id, id_group, title, text, src)