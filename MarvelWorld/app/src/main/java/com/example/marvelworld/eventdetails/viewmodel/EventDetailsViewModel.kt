package com.example.marvelworld.eventdetails.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.marvelworld.eventdetails.respository.EventDetailsRepository
import com.example.marvelworld.favorite.respository.FavoriteRepository
import com.example.marvelworld.reusablecomponents.horizontallist.HorizontalListItem
import com.example.marvelworld.reusablecomponents.horizontallist.HorizontalListUtils
import com.example.marvelworld.util.ResourceType
import kotlinx.coroutines.Dispatchers

class EventDetailsViewModel(
    private val eventRepository: EventDetailsRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {
    fun getEvent(eventId: Int) = liveData(Dispatchers.IO) {
        val response = eventRepository.getEvent(eventId)
        val event = response.data.results[0]
        event.isFavorite = favoriteRepository.isFavorite(event.id, ResourceType.EVENT)
        emit(response.data.results[0])
    }

    fun getEventCharacters(eventId: Int) = liveData(Dispatchers.IO) {
        val response = eventRepository.getEventCharacters(eventId)
        emit(response.data.results.map { character ->
            HorizontalListItem(
                character.id,
                character.name,
                HorizontalListUtils.CHARACTER
            )
        })
    }

    fun getEventComics(eventId: Int) = liveData(Dispatchers.IO) {
        val response = eventRepository.getEventComics(eventId)
        emit(response.data.results.map { comic ->
            HorizontalListItem(
                comic.id,
                comic.title,
                HorizontalListUtils.COMIC
            )
        })
    }

    fun getEventStories(eventId: Int) = liveData(Dispatchers.IO) {
        val response = eventRepository.getEventStories(eventId)
        emit(response.data.results.map { story ->
            HorizontalListItem(
                story.id,
                story.title,
                HorizontalListUtils.STORY
            )
        })
    }

    fun getEventSeries(eventId: Int) = liveData(Dispatchers.IO) {
        val response = eventRepository.getEventSeries(eventId)
        emit(response.data.results.map { series ->
            HorizontalListItem(
                series.id,
                series.title,
                HorizontalListUtils.SERIES
            )
        })
    }

    fun getEventCreators(eventId: Int) = liveData(Dispatchers.IO) {
        val response = eventRepository.getEventCreators(eventId)
        emit(response.data.results.map { creator ->
            HorizontalListItem(
                creator.id,
                creator.fullName,
                HorizontalListUtils.CREATOR
            )
        })
    }

    @Suppress("UNCHECKED_CAST")
    class EventDetailsViewModelFactory(
        private val eventRepository: EventDetailsRepository,
        private val favoriteRepository: FavoriteRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return EventDetailsViewModel(eventRepository, favoriteRepository) as T
        }
    }
}