package com.example.marvelworld.favorite.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.marvelworld.util.ResourceType

@Entity
data class Favorite(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo
    val userId: String,

    @ColumnInfo
    val resourceId: Int,

    @ColumnInfo
    val type: ResourceType,

    @ColumnInfo
    val title: String,

    @ColumnInfo
    val imagePath: String?,

    @ColumnInfo
    val imageExtension: String?
) {
    constructor(
        userId: String,
        resourceId: Int,
        type: ResourceType,
        title: String,
        imagePath: String?,
        imageExtension: String?
    ) : this(0, userId, resourceId, type, title, imagePath, imageExtension)
}
