package com.example.marvelworld.detailcard.repository

import com.example.marvelworld.favorite.dao.FavoriteDao
import com.example.marvelworld.favorite.models.Favorite
import com.example.marvelworld.util.ResourceType

class DetailCardRepository(private val favoriteDao: FavoriteDao) {

    suspend fun addFavorite(favorite: Favorite) = favoriteDao.addFavorite(favorite)

    suspend fun removeFavorite(userId: String, resourceId: Int, type: ResourceType) =
        favoriteDao.removeFavorite(userId, resourceId, type)

    suspend fun isFavorite(userId: String, resourceId: Int, type: ResourceType) =
        favoriteDao.isFavorite(userId, resourceId, type)
}