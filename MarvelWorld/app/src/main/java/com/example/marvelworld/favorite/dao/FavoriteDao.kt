package com.example.marvelworld.favorite.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.marvelworld.favorite.models.Favorite
import com.example.marvelworld.util.ResourceType

@Dao
interface FavoriteDao {

    @Insert
    suspend fun addFavorite(favorite: Favorite)

    @Query("DELETE FROM Favorite WHERE resourceId = :resourceId AND type = :type")
    suspend fun removeFavorite(resourceId: Int, type: ResourceType)

    @Query("SELECT * FROM Favorite WHERE type = :type")
    suspend fun getFavorites(type: ResourceType): List<Favorite>

    @Query("SELECT EXISTS(SELECT 1 FROM Favorite WHERE resourceId = :resourceId AND type = :type)")
    suspend fun isFavorite(resourceId: Int, type: ResourceType): Boolean
}