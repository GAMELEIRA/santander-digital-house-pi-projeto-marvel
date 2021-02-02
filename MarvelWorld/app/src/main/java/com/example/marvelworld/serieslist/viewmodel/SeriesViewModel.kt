package com.example.marvelworld.serieslist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.marvelworld.api.models.Image
import com.example.marvelworld.favorite.models.Favorite
import com.example.marvelworld.favorite.respository.FavoriteRepository
import com.example.marvelworld.filters.models.Filter
import com.example.marvelworld.serieslist.models.Series
import com.example.marvelworld.serieslist.repository.SeriesRepository
import com.example.marvelworld.util.ResourceType
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers

class SeriesViewModel(
    private val seriesRepository: SeriesRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {
    private val userId by lazy { FirebaseAuth.getInstance().currentUser!!.uid }

    private var title: String? = null
    private var characters = listOf<Int>()
    private var comics = listOf<Int>()
    private var events = listOf<Int>()
    private var creators = listOf<Int>()
    private var offset = 0
    private val limit = 20
    var total = 0

    private var offsetFavorite = 0
    private val limitFavorite = 20
    var totalFavorite = 0

    fun getSeries() = liveData(Dispatchers.IO) {
        val response =
            seriesRepository.getSeries(offset, limit, title, characters, comics, events, creators)

        offset = response.data.offset + response.data.count
        total = response.data.total

        response.data.results.forEach {
            it.isFavorite = favoriteRepository.isFavorite(userId, it.id, ResourceType.SERIES)
        }

        emit(response.data.results)
    }

    fun updateSeries(series: MutableList<Series>) = liveData(Dispatchers.IO) {
        series.forEach {
            it.isFavorite = favoriteRepository.isFavorite(userId, it.id, ResourceType.SERIES)
        }

        emit(true)
    }

    fun getFavoriteSeries() = liveData(Dispatchers.IO) {
        val favorites = favoriteRepository.getFavorites(
            offsetFavorite,
            limitFavorite,
            userId,
            ResourceType.SERIES
        )
        val series = mutableListOf<Series>()

        totalFavorite = favoriteRepository.countFavorites(userId, ResourceType.COMIC)
        offsetFavorite += favorites.size

        favorites.forEach {
            val oneSeries = Series(
                it.resourceId,
                it.title,
                "",
                listOf(),
                Image(it.imagePath!!, it.imageExtension!!),
                true
            )
            oneSeries.isFavorite = true
            series.add(oneSeries)
        }

        emit(series)
    }

    fun updateFavoriteSeries(series: MutableList<Series>) = liveData(Dispatchers.IO) {
        val seriesToRemove = mutableListOf<Series>()
        series.forEach {
            val isFavorite = favoriteRepository.isFavorite(userId, it.id, ResourceType.SERIES)
            if (!isFavorite) seriesToRemove.add(it)
        }
        emit(seriesToRemove)
    }

    fun addFavorite(resourceId: Int, title: String, imagePath: String?, imageExtension: String?) =
        liveData(Dispatchers.IO) {
            favoriteRepository.addFavorite(
                Favorite(
                    userId,
                    resourceId,
                    ResourceType.SERIES,
                    title,
                    imagePath,
                    imageExtension
                )
            )
            emit(true)
        }

    fun removeFavorite(resourceId: Int) = liveData(Dispatchers.IO) {
        favoriteRepository.removeFavorite(userId, resourceId, ResourceType.SERIES)
        emit(true)
    }

    fun isFavorite(resourceId: Int) = liveData(Dispatchers.IO) {
        val result = favoriteRepository.isFavorite(userId, resourceId, ResourceType.SERIES)
        emit(result)
    }

    fun applyFilter(filter: Filter) {
        title = filter.text
        characters = filter.filterMap[Filter.CHARACTER]?.map { c -> c.id } ?: listOf()
        comics = filter.filterMap[Filter.COMIC]?.map { c -> c.id } ?: listOf()
        events = filter.filterMap[Filter.EVENT]?.map { e -> e.id } ?: listOf()
        creators = filter.filterMap[Filter.CREATOR]?.map { s -> s.id } ?: listOf()

        offset = 0
        total = 0
    }

    @Suppress("UNCHECKED_CAST")
    class SeriesViewModelFactory(
        private val seriesRepository: SeriesRepository,
        private val favoriteRepository: FavoriteRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return SeriesViewModel(seriesRepository, favoriteRepository) as T
        }
    }
}