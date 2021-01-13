package com.example.marvelworld.detailcard.repository

import com.example.marvelworld.favorite.dao.FavoriteDao
import com.example.marvelworld.favorite.models.Favorite
import com.example.marvelworld.util.ResourceType

class DetailCardRepository(private val favoriteDao: FavoriteDao) {

    suspend fun addFavorite(favorite: Favorite) = favoriteDao.addFavorite(favorite)

    suspend fun removeFavorite(resourceId: Int, type: ResourceType) = favoriteDao.removeFavorite(resourceId, type)

    suspend fun isFavorite(resourceId: Int, type: ResourceType) = favoriteDao.isFavorite(resourceId, type)
}