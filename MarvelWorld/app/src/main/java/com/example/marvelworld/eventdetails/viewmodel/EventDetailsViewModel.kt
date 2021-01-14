package com.example.marvelworld.eventdetails.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.marvelworld.eventdetails.respository.EventDetailsRepository
import com.example.marvelworld.favorite.respository.FavoriteRepository
import com.example.marvelworld.horizontallist.HorizontalListItem
import com.example.marvelworld.util.InfiniteScrollable
import com.example.marvelworld.util.ResourceType
import kotlinx.coroutines.Dispatchers

class EventDetailsViewModel(
    private val eventRepository: EventDetailsRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel(), InfiniteScrollable {

    override var offset = 0
    override val limit = 20
    override var total = 0

    fun getEvent(eventId: Int) = liveData(Dispatchers.IO) {
        val response = eventRepository.getEvent(eventId)
        val event = response.data.results[0]
        event.isFavorite = favoriteRepository.isFavorite(event.id, ResourceType.EVENT)
        emit(response.data.results[0])
    }

    override fun getCharacters(resourceId: Int) = liveData(Dispatchers.IO) {
        val response = eventRepository.getEventCharacters(offset, limit, resourceId)

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
        val response = eventRepository.getEventComics(offset, limit, resourceId)

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
        val response = eventRepository.getEventStories(offset, limit, resourceId)

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

    override fun getSeries(resourceId: Int) = liveData(Dispatchers.IO) {
        val response = eventRepository.getEventSeries(offset, limit, resourceId)

        offset = response.data.offset + response.data.count
        total = response.data.total

        emit(response.data.results.map { series ->
            HorizontalListItem(
                series.id,
                series.title,
                ResourceType.SERIES
            )
        })
    }

    override fun getCreators(resourceId: Int) = liveData(Dispatchers.IO) {
        val response = eventRepository.getEventCreators(offset, limit, resourceId)

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
    class EventDetailsViewModelFactory(
        private val eventRepository: EventDetailsRepository,
        private val favoriteRepository: FavoriteRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return EventDetailsViewModel(eventRepository, favoriteRepository) as T
        }
    }
}