package com.example.marvelworld.eventdetails.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.marvelworld.eventdetails.respository.EventDetailsRepository
import com.example.marvelworld.reusablecomponents.horizontallist.HorizontalListItem
import com.example.marvelworld.reusablecomponents.horizontallist.HorizontalListUtils
import kotlinx.coroutines.Dispatchers

@Suppress("UNCHECKED_CAST")
class EventDetailsViewModel(
    private val repository: EventDetailsRepository
) : ViewModel() {
    fun getEvent(eventId: Int) = liveData(Dispatchers.IO) {
        val response = repository.getEvent(eventId)
        emit(response.data.results[0])
    }

    fun getEventCharacters(eventId: Int) = liveData(Dispatchers.IO) {
        val response = repository.getEventCharacters(eventId)
        emit(response.data.results.map { character ->
            HorizontalListItem(
                character.id,
                character.name,
                HorizontalListUtils.CHARACTER
            )
        })
    }

    fun getEventComics(eventId: Int) = liveData(Dispatchers.IO) {
        val response = repository.getEventComics(eventId)
        emit(response.data.results.map { comic ->
            HorizontalListItem(
                comic.id,
                comic.title,
                HorizontalListUtils.COMIC
            )
        })
    }

    fun getEventStories(eventId: Int) = liveData(Dispatchers.IO) {
        val response = repository.getEventStories(eventId)
        emit(response.data.results.map { story ->
            HorizontalListItem(
                story.id,
                story.title,
                HorizontalListUtils.STORY
            )
        })
    }

    fun getEventSeries(eventId: Int) = liveData(Dispatchers.IO) {
        val response = repository.getEventSeries(eventId)
        emit(response.data.results.map { series ->
            HorizontalListItem(
                series.id,
                series.title,
                HorizontalListUtils.SERIES
            )
        })
    }

    fun getEventCreators(eventId: Int) = liveData(Dispatchers.IO) {
        val response = repository.getEventCreators(eventId)
        emit(response.data.results.map { creator ->
            HorizontalListItem(
                creator.id,
                creator.fullName,
                HorizontalListUtils.CREATOR
            )
        })
    }

    class EventDetailsViewModelFactory(
        private val repository: EventDetailsRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return EventDetailsViewModel(repository) as T
        }
    }
}