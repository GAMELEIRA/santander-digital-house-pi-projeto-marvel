package com.example.marvelworld.seriesdetails.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.marvelworld.favorite.respository.FavoriteRepository
import com.example.marvelworld.horizontallist.HorizontalListItem
import com.example.marvelworld.seriesdetails.respository.SeriesDetailsRepository
import com.example.marvelworld.util.InfiniteScrollable
import com.example.marvelworld.util.ResourceType
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers

class SeriesDetailsViewModel(
    private val seriesRepository: SeriesDetailsRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel(), InfiniteScrollable {
    private val userId by lazy { FirebaseAuth.getInstance().currentUser!!.uid }

    override var offset = 0
    override val limit = 20
    override var total = 0

    fun getOneSeries(seriesId: Int) = liveData(Dispatchers.IO) {
        val response = seriesRepository.getOneSeries(seriesId)
        val series = response.data.results[0]
        series.isFavorite = favoriteRepository.isFavorite(userId, series.id, ResourceType.SERIES)
        emit(response.data.results[0])
    }

    override fun getCharacters(resourceId: Int) = liveData(Dispatchers.IO) {
        val response = seriesRepository.getSeriesCharacters(offset, limit, resourceId)

        offset = response.data.offset + response.data.count
        total = response.data.total

        emit(response.data.results.map { character ->
            HorizontalListItem(
                character.id,
                character.name,
                ResourceType.CHARACTER
            )
        })
    }

    override fun getComics(resourceId: Int) = liveData(Dispatchers.IO) {
        val response = seriesRepository.getSeriesComics(offset, limit, resourceId)

        offset = response.data.offset + response.data.count
        total = response.data.total

        emit(response.data.results.map { comic ->
            HorizontalListItem(
                comic.id,
                comic.title,
                ResourceType.COMIC
            )
        })
    }

    override fun getStories(resourceId: Int) = liveData(Dispatchers.IO) {
        val response = seriesRepository.getSeriesStories(offset, limit, resourceId)

        offset = response.data.offset + response.data.count
        total = response.data.total

        emit(response.data.results.map { story ->
            HorizontalListItem(
                story.id,
                story.title,
                ResourceType.STORY
            )
        })
    }

    override fun getEvents(resourceId: Int) = liveData(Dispatchers.IO) {
        val response = seriesRepository.getSeriesEvents(offset, limit, resourceId)

        offset = response.data.offset + response.data.count
        total = response.data.total

        emit(response.data.results.map { event ->
            HorizontalListItem(
                event.id,
                event.title,
                ResourceType.EVENT
            )
        })
    }

    override fun getCreators(resourceId: Int) = liveData(Dispatchers.IO) {
        val response = seriesRepository.getSeriesCreators(offset, limit, resourceId)

        offset = response.data.offset + response.data.count
        total = response.data.total

        emit(response.data.results.map { creator ->
            HorizontalListItem(
                creator.id,
                creator.fullName,
                ResourceType.CREATOR
            )
        })
    }

    @Suppress("UNCHECKED_CAST")
    class SeriesDetailsViewModelFactory(
        private val seriesRepository: SeriesDetailsRepository,
        private val favoriteRepository: FavoriteRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return SeriesDetailsViewModel(seriesRepository, favoriteRepository) as T
        }
    }
}