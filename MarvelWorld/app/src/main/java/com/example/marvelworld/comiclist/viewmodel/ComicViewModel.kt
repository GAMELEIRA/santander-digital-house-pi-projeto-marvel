package com.example.marvelworld.comiclist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.marvelworld.comiclist.models.Comic
import com.example.marvelworld.comiclist.repository.ComicRepository
import com.example.marvelworld.favorite.models.Favorite
import com.example.marvelworld.favorite.respository.FavoriteRepository
import com.example.marvelworld.filters.models.Filter
import com.example.marvelworld.util.ResourceType
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers

class ComicViewModel(
    private val comicRepository: ComicRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {
    private val userId by lazy { FirebaseAuth.getInstance().currentUser!!.uid }

    private var title: String? = null
    private var characters = listOf<Int>()
    private var events = listOf<Int>()
    private var series = listOf<Int>()
    private var creators = listOf<Int>()
    private var offset = 0
    private val limit = 20
    var total = 0

    fun getComics() = liveData(Dispatchers.IO) {
        val response =
            comicRepository.getComics(offset, limit, title, characters, events, series, creators)

        offset = response.data.offset + response.data.count
        total = response.data.total

        response.data.results.forEach {
            it.isFavorite = favoriteRepository.isFavorite(userId, it.id, ResourceType.COMIC)
        }

        emit(response.data.results)
    }

    fun updateComics(comics: MutableList<Comic>) = liveData(Dispatchers.IO) {
        comics.forEach {
            it.isFavorite = favoriteRepository.isFavorite(userId, it.id, ResourceType.COMIC)
        }

        emit(true)
    }

    fun getFavoriteComics() = liveData(Dispatchers.IO) {
        val favorites = favoriteRepository.getFavorites(userId, ResourceType.COMIC)
        val comics = mutableListOf<Comic>()

        favorites.forEach {
            val comic = comicRepository.getComic(it.resourceId).data.results[0]
            comic.isFavorite = true
            comics.add(comic)
        }

        emit(comics)
    }

    fun updateFavoriteComics(comics: MutableList<Comic>) = liveData(Dispatchers.IO) {
        val comicsToRemove = mutableListOf<Comic>()
        comics.forEach {
            val isFavorite = favoriteRepository.isFavorite(userId, it.id, ResourceType.COMIC)
            if (!isFavorite) comicsToRemove.add(it)
        }
        emit(comicsToRemove)
    }

    fun addFavorite(resourceId: Int) = liveData(Dispatchers.IO) {
        favoriteRepository.addFavorite(Favorite(userId, resourceId, ResourceType.COMIC))
        emit(true)
    }

    fun removeFavorite(resourceId: Int) = liveData(Dispatchers.IO) {
        favoriteRepository.removeFavorite(userId, resourceId, ResourceType.COMIC)
        emit(true)
    }

    fun isFavorite(resourceId: Int) = liveData(Dispatchers.IO) {
        val result = favoriteRepository.isFavorite(userId, resourceId, ResourceType.COMIC)
        emit(result)
    }

    fun applyFilter(filter: Filter) {
        title = filter.text
        characters = filter.filterMap[Filter.CHARACTER]?.map { c -> c.id } ?: listOf()
        events = filter.filterMap[Filter.EVENT]?.map { e -> e.id } ?: listOf()
        series = filter.filterMap[Filter.SERIES]?.map { s -> s.id } ?: listOf()
        creators = filter.filterMap[Filter.CREATOR]?.map { s -> s.id } ?: listOf()

        offset = 0
        total = 0
    }

    @Suppress("UNCHECKED_CAST")
    class ComicViewModelFactory(
        private val comicRepository: ComicRepository,
        private val favoriteRepository: FavoriteRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ComicViewModel(comicRepository, favoriteRepository) as T
        }
    }
}