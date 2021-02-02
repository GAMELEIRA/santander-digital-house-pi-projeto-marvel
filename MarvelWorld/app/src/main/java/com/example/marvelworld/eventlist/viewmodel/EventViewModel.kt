package com.example.marvelworld.eventlist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.marvelworld.api.models.Image
import com.example.marvelworld.eventlist.models.Event
import com.example.marvelworld.eventlist.repository.EventRepository
import com.example.marvelworld.favorite.models.Favorite
import com.example.marvelworld.favorite.respository.FavoriteRepository
import com.example.marvelworld.filters.models.Filter
import com.example.marvelworld.util.ResourceType
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers

class EventViewModel(
    private val eventRepository: EventRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {
    private val userId by lazy { FirebaseAuth.getInstance().currentUser!!.uid }

    private var name: String? = null
    private var characters = listOf<Int>()
    private var comics = listOf<Int>()
    private var events = listOf<Int>()
    private var series = listOf<Int>()
    private var offset = 0
    private val limit = 20
    var total = 0

    private var offsetFavorite = 0
    private val limitFavorite = 20
    var totalFavorite = 0

    fun getEvents() = liveData(Dispatchers.IO) {
        val response =
            eventRepository.getEvents(offset, limit, name, characters, comics, events, series)

        offset = response.data.offset + response.data.count
        total = response.data.total

        response.data.results.forEach {
            it.isFavorite = favoriteRepository.isFavorite(userId, it.id, ResourceType.EVENT)
        }

        emit(response.data.results)
    }

    fun updateEvents(events: MutableList<Event>) = liveData(Dispatchers.IO) {
        events.forEach {
            it.isFavorite = favoriteRepository.isFavorite(userId, it.id, ResourceType.EVENT)
        }

        emit(true)
    }

    fun getFavoriteEvents() = liveData(Dispatchers.IO) {
        val favorites = favoriteRepository.getFavorites(
            offsetFavorite,
            limitFavorite,
            userId,
            ResourceType.EVENT
        )
        val events = mutableListOf<Event>()

        totalFavorite = favoriteRepository.countFavorites(userId, ResourceType.COMIC)
        offsetFavorite += favorites.size

        favorites.forEach {
            val event = Event(
                it.resourceId,
                it.title,
                "",
                listOf(),
                Image(it.imagePath!!, it.imageExtension!!),
                true
            )
            event.isFavorite = true
            events.add(event)
        }

        emit(events)
    }

    fun updateFavoriteEvents(events: MutableList<Event>) = liveData(Dispatchers.IO) {
        val eventsToRemove = mutableListOf<Event>()
        events.forEach {
            val isFavorite = favoriteRepository.isFavorite(userId, it.id, ResourceType.EVENT)
            if (!isFavorite) eventsToRemove.add(it)
        }
        emit(eventsToRemove)
    }

    fun addFavorite(resourceId: Int, title: String, imagePath: String?, imageExtension: String?) =
        liveData(Dispatchers.IO) {
            favoriteRepository.addFavorite(
                Favorite(
                    userId,
                    resourceId,
                    ResourceType.EVENT,
                    title,
                    imagePath,
                    imageExtension
                )
            )
            emit(true)
        }

    fun removeFavorite(resourceId: Int) = liveData(Dispatchers.IO) {
        favoriteRepository.removeFavorite(userId, resourceId, ResourceType.EVENT)
        emit(true)
    }

    fun isFavorite(resourceId: Int) = liveData(Dispatchers.IO) {
        val result = favoriteRepository.isFavorite(userId, resourceId, ResourceType.EVENT)
        emit(result)
    }

    fun applyFilter(filter: Filter) {
        name = filter.text
        characters = filter.filterMap[Filter.CHARACTER]?.map { c -> c.id } ?: listOf()
        comics = filter.filterMap[Filter.COMIC]?.map { c -> c.id } ?: listOf()
        events = filter.filterMap[Filter.EVENT]?.map { e -> e.id } ?: listOf()
        series = filter.filterMap[Filter.SERIES]?.map { s -> s.id } ?: listOf()

        offset = 0
        total = 0
    }

    @Suppress("UNCHECKED_CAST")
    class EventViewModelFactory(
        private val eventRepository: EventRepository,
        private val favoriteRepository: FavoriteRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return EventViewModel(eventRepository, favoriteRepository) as T
        }
    }
}