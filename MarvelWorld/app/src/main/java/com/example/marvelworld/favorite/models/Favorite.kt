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
    val resourceId: Int,

    @ColumnInfo
    val type: ResourceType
) {
    constructor(resourceId: Int, type: ResourceType): this(0, resourceId, type)
}