package com.example.marvelworld.seriesdetails.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.marvelworld.reusablecomponents.horizontallist.HorizontalListItem
import com.example.marvelworld.reusablecomponents.horizontallist.HorizontalListUtils
import com.example.marvelworld.seriesdetails.respository.SeriesDetailsRepository
import kotlinx.coroutines.Dispatchers

@Suppress("UNCHECKED_CAST")
class SeriesDetailsViewModel(
    private val repository: SeriesDetailsRepository
) : ViewModel() {
    fun getOneSeries(seriesId: Int) = liveData(Dispatchers.IO) {
        val response = repository.getOneSeries(seriesId)
        emit(response.data.results[0])
    }

    fun getSeriesCharacters(seriesId: Int) = liveData(Dispatchers.IO) {
        val response = repository.getSeriesCharacters(seriesId)
        emit(response.data.results.map { character ->
            HorizontalListItem(
                character.id,
                character.name,
                HorizontalListUtils.CHARACTER
            )
        })
    }

    fun getSeriesComics(eventId: Int) = liveData(Dispatchers.IO) {
        val response = repository.getSeriesComics(eventId)
        emit(response.data.results.map { comic ->
            HorizontalListItem(
                comic.id,
                comic.title,
                HorizontalListUtils.COMIC
            )
        })
    }

    fun getSeriesStories(eventId: Int) = liveData(Dispatchers.IO) {
        val response = repository.getSeriesStories(eventId)
        emit(response.data.results.map { story ->
            HorizontalListItem(
                story.id,
                story.title,
                HorizontalListUtils.STORY
            )
        })
    }

    fun getSeriesEvents(eventId: Int) = liveData(Dispatchers.IO) {
        val response = repository.getSeriesEvents(eventId)
        emit(response.data.results.map { event ->
            HorizontalListItem(
                event.id,
                event.title,
                HorizontalListUtils.EVENT
            )
        })
    }

    fun getSeriesCreators(eventId: Int) = liveData(Dispatchers.IO) {
        val response = repository.getSeriesCreators(eventId)
        emit(response.data.results.map { creator ->
            HorizontalListItem(
                creator.id,
                creator.fullName,
                HorizontalListUtils.CREATOR
            )
        })
    }

    class SeriesDetailsViewModelFactory(
        private val repository: SeriesDetailsRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return SeriesDetailsViewModel(repository) as T
        }
    }
}