package com.example.marvelworld.creatorlist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.marvelworld.api.models.Image
import com.example.marvelworld.creatorlist.models.Creator
import com.example.marvelworld.creatorlist.respository.CreatorRepository
import com.example.marvelworld.favorite.models.Favorite
import com.example.marvelworld.favorite.respository.FavoriteRepository
import com.example.marvelworld.filters.models.Filter
import com.example.marvelworld.util.ResourceType
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers

class CreatorViewModel(
    private val creatorRepository: CreatorRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {
    private val userId by lazy { FirebaseAuth.getInstance().currentUser!!.uid }

    private var name: String? = null
    private var comics = listOf<Int>()
    private var events = listOf<Int>()
    private var series = listOf<Int>()
    private var offset = 0
    private val limit = 20
    var total = 0

    private var offsetFavorite = 0
    private val limitFavorite = 20
    var totalFavorite = 0

    fun getCreators() = liveData(Dispatchers.IO) {
        val response = creatorRepository.getCreators(offset, limit, name, comics, events, series)

        offset = response.data.offset + response.data.count
        total = response.data.total

        response.data.results.forEach {
            it.isFavorite = favoriteRepository.isFavorite(userId, it.id, ResourceType.CREATOR)
        }

        emit(response.data.results)
    }

    fun updateCreators(creators: MutableList<Creator>) = liveData(Dispatchers.IO) {
        creators.forEach {
            it.isFavorite = favoriteRepository.isFavorite(userId, it.id, ResourceType.CREATOR)
        }

        emit(true)
    }

    fun getFavoriteCreators() = liveData(Dispatchers.IO) {
        val favorites = favoriteRepository.getFavorites(
            offsetFavorite,
            limitFavorite,
            userId,
            ResourceType.CREATOR
        )
        val creators = mutableListOf<Creator>()

        totalFavorite = favoriteRepository.countFavorites(userId, ResourceType.COMIC)
        offsetFavorite += favorites.size

        favorites.forEach {
            val creator = Creator(
                it.resourceId,
                it.title,
                listOf(),
                Image(it.imagePath!!, it.imageExtension!!),
                true
            )
            creator.isFavorite = true
            creators.add(creator)
        }

        emit(creators)
    }

    fun updateFavoriteCreators(creators: MutableList<Creator>) = liveData(Dispatchers.IO) {
        val creatorsToRemove = mutableListOf<Creator>()
        creators.forEach {
            val isFavorite = favoriteRepository.isFavorite(userId, it.id, ResourceType.CREATOR)
            if (!isFavorite) creatorsToRemove.add(it)
        }
        emit(creatorsToRemove)
    }

    fun addFavorite(resourceId: Int, title: String, imagePath: String?, imageExtension: String?) =
        liveData(Dispatchers.IO) {
            favoriteRepository.addFavorite(
                Favorite(
                    userId,
                    resourceId,
                    ResourceType.CREATOR,
                    title,
                    imagePath,
                    imageExtension
                )
            )
            emit(true)
        }

    fun removeFavorite(resourceId: Int) = liveData(Dispatchers.IO) {
        favoriteRepository.removeFavorite(userId, resourceId, ResourceType.CREATOR)
        emit(true)
    }

    fun isFavorite(resourceId: Int) = liveData(Dispatchers.IO) {
        val result = favoriteRepository.isFavorite(userId, resourceId, ResourceType.CREATOR)
        emit(result)
    }

    fun applyFilter(filter: Filter) {
        name = filter.text
        comics = filter.filterMap[Filter.COMIC]?.map { c -> c.id } ?: listOf()
        events = filter.filterMap[Filter.EVENT]?.map { e -> e.id } ?: listOf()
        series = filter.filterMap[Filter.SERIES]?.map { s -> s.id } ?: listOf()

        offset = 0
        total = 0
    }

    @Suppress("UNCHECKED_CAST")
    class CreatorViewModelFactory(
        private val creatorRepository: CreatorRepository,
        private val favoriteRepository: FavoriteRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CreatorViewModel(creatorRepository, favoriteRepository) as T
        }
    }
}