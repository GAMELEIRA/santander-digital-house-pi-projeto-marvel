package com.example.marvelworld.favorite.respository

import com.example.marvelworld.favorite.dao.FavoriteDao
import com.example.marvelworld.favorite.models.Favorite
import com.example.marvelworld.util.ResourceType

class FavoriteRepository(private val favoriteDao: FavoriteDao) {

    suspend fun addFavorite(favorite: Favorite) = favoriteDao.addFavorite(favorite)

    suspend fun removeFavorite(userId: String, resourceId: Int, type: ResourceType) =
        favoriteDao.removeFavorite(userId, resourceId, type)

    suspend fun getFavorites(offset: Int, limit: Int, userId: String, type: ResourceType) =
        favoriteDao.getFavorites(offset, limit, userId, type)

    suspend fun isFavorite(userId: String, resourceId: Int, type: ResourceType) =
        favoriteDao.isFavorite(userId, resourceId, type)

    fun countFavorites(userId: String, type: ResourceType) =
        favoriteDao.countFavorites(userId, type)
}