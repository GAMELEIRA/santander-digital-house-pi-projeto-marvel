package com.example.marvelworld.seriesdetails.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.marvelworld.favorite.respository.FavoriteRepository
import com.example.marvelworld.reusablecomponents.horizontallist.HorizontalListItem
import com.example.marvelworld.reusablecomponents.horizontallist.HorizontalListUtils
import com.example.marvelworld.seriesdetails.respository.SeriesDetailsRepository
import com.example.marvelworld.util.ResourceType
import kotlinx.coroutines.Dispatchers

class SeriesDetailsViewModel(
    private val seriesRepository: SeriesDetailsRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {
    fun getOneSeries(seriesId: Int) = liveData(Dispatchers.IO) {
        val response = seriesRepository.getOneSeries(seriesId)
        val series = response.data.results[0]
        series.isFavorite = favoriteRepository.isFavorite(series.id, ResourceType.SERIES)
        emit(response.data.results[0])
    }

    fun getSeriesCharacters(seriesId: Int) = liveData(Dispatchers.IO) {
        val response = seriesRepository.getSeriesCharacters(seriesId)
        emit(response.data.results.map { character ->
            HorizontalListItem(
                character.id,
                character.name,
                HorizontalListUtils.CHARACTER
            )
        })
    }

    fun getSeriesComics(seriesId: Int) = liveData(Dispatchers.IO) {
        val response = seriesRepository.getSeriesComics(seriesId)
        emit(response.data.results.map { comic ->
            HorizontalListItem(
                comic.id,
                comic.title,
                HorizontalListUtils.COMIC
            )
        })
    }

    fun getSeriesStories(seriesId: Int) = liveData(Dispatchers.IO) {
        val response = seriesRepository.getSeriesStories(seriesId)
        emit(response.data.results.map { story ->
            HorizontalListItem(
                story.id,
                story.title,
                HorizontalListUtils.STORY
            )
        })
    }

    fun getSeriesEvents(seriesId: Int) = liveData(Dispatchers.IO) {
        val response = seriesRepository.getSeriesEvents(seriesId)
        emit(response.data.results.map { event ->
            HorizontalListItem(
                event.id,
                event.title,
                HorizontalListUtils.EVENT
            )
        })
    }

    fun getSeriesCreators(seriesId: Int) = liveData(Dispatchers.IO) {
        val response = seriesRepository.getSeriesCreators(seriesId)
        emit(response.data.results.map { creator ->
            HorizontalListItem(
                creator.id,
                creator.fullName,
                HorizontalListUtils.CREATOR
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