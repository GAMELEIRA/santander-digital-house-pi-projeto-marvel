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

    @Query("DELETE FROM Favorite WHERE resourceId = :resourceId AND type = :type AND userId = :userId")
    suspend fun removeFavorite(userId: String, resourceId: Int, type: ResourceType)

    @Query("DELETE FROM Favorite WHERE userId = :userId")
    suspend fun removeFavorites(userId: String)

    @Query("SELECT * FROM Favorite WHERE type = :type AND userId = :userId ORDER BY id LIMIT :limit OFFSET :offset")
    suspend fun getFavorites(offset: Int, limit: Int, userId: String, type: ResourceType): List<Favorite>

    @Query("SELECT EXISTS(SELECT 1 FROM Favorite WHERE resourceId = :resourceId AND type = :type AND userId = :userId)")
    suspend fun isFavorite(userId: String, resourceId: Int, type: ResourceType): Boolean

    @Query("SELECT COUNT(1) FROM Favorite WHERE type = :type AND userId = :userId")
    suspend fun countFavorites(userId: String, type: ResourceType): Int
}