package com.example.marvelworld.favorite.respository

import com.example.marvelworld.favorite.dao.FavoriteDao
import com.example.marvelworld.favorite.models.Favorite
import com.example.marvelworld.util.ResourceType

class FavoriteRepository(private val favoriteDao: FavoriteDao) {

    suspend fun addFavorite(favorite: Favorite) = favoriteDao.addFavorite(favorite)

    suspend fun removeFavorite(userId: String, resourceId: Int, type: ResourceType) =
        favoriteDao.removeFavorite(userId, resourceId, type)

    suspend fun getFavorites(userId: String, type: ResourceType) =
        favoriteDao.getFavorites(userId, type)

    suspend fun isFavorite(userId: String, resourceId: Int, type: ResourceType) =
        favoriteDao.isFavorite(userId, resourceId, type)
}