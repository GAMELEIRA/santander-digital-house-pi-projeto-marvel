package com.example.marvelworld.eventlist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.marvelworld.eventlist.repository.EventRepository
import com.example.marvelworld.filters.models.Filter
import kotlinx.coroutines.Dispatchers

class EventViewModel(
    private val repository: EventRepository
) : ViewModel() {
    private var name: String? = null
    private var characters = listOf<Int>()
    private var comics = listOf<Int>()
    private var events = listOf<Int>()
    private var series = listOf<Int>()

    fun getEvents() = liveData(Dispatchers.IO) {
        val response = repository.getEvents(name, characters, comics, events, series)
        emit(response.data.results)
    }

    fun applyFilter(filter: Filter) {
        name = filter.text
        characters = filter.filterMap[Filter.CHARACTER]?.map { c -> c.id } ?: listOf()
        comics = filter.filterMap[Filter.COMIC]?.map { c -> c.id } ?: listOf()
        events = filter.filterMap[Filter.EVENT]?.map { e -> e.id } ?: listOf()
        series = filter.filterMap[Filter.SERIES]?.map { s -> s.id } ?: listOf()
    }

    class EventViewModelFactory(
        private val repository: EventRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return EventViewModel(repository) as T
        }
    }
}