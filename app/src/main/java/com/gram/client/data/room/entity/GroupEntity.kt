package com.gram.client.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gram.client.domain.database.models.GroupModel

@Entity(tableName = "group_stories")
data class GroupEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id_group")
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String?,
    @ColumnInfo(name = "img_src")
    val img_src: String?,
)

fun GroupEntity.toDomain() = GroupModel(id, name, img_src)
