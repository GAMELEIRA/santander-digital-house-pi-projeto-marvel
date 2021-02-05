package com.example.marvelworld.myprofile.repository

import com.example.marvelworld.favorite.dao.FavoriteDao

class MyProfileRepository(private val favoriteDao: FavoriteDao) {
    suspend fun removeFavorites(userId: String) =
        favoriteDao.removeFavorites(userId)
}