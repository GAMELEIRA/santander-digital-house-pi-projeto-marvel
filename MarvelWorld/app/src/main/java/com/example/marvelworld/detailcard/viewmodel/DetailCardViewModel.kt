package com.example.marvelworld.detailcard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.marvelworld.detailcard.repository.DetailCardRepository
import com.example.marvelworld.favorite.models.Favorite
import com.example.marvelworld.util.ResourceType
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers

class DetailCardViewModel(
    private val repository: DetailCardRepository
) : ViewModel() {
    private val userId by lazy { FirebaseAuth.getInstance().currentUser!!.uid }

    fun addFavorite(
        resourceId: Int,
        type: ResourceType,
        title: String,
        imagePath: String?,
        imageExtension: String?
    ) = liveData(Dispatchers.IO) {
        repository.addFavorite(Favorite(userId, resourceId, type, title, imagePath, imageExtension))
        emit(true)
    }

    fun removeFavorite(resourceId: Int, type: ResourceType) = liveData(Dispatchers.IO) {
        repository.removeFavorite(userId, resourceId, type)
        emit(true)
    }

    fun isFavorite(resourceId: Int, type: ResourceType) = liveData(Dispatchers.IO) {
        val result = repository.isFavorite(userId, resourceId, type)
        emit(result)
    }

    @Suppress("UNCHECKED_CAST")
    class DetailCardViewModelFactory(
        private val repository: DetailCardRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return DetailCardViewModel(repository) as T
        }
    }
}