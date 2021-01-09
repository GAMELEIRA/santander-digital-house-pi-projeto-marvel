package com.example.marvelworld.storylist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.marvelworld.filters.models.Filter
import com.example.marvelworld.storylist.repository.StoryRepository
import kotlinx.coroutines.Dispatchers

class StoryViewModel(
    private val repository: StoryRepository
) : ViewModel() {
    private var characters = listOf<Int>()
    private var comics = listOf<Int>()
    private var events = listOf<Int>()
    private var series = listOf<Int>()
    private var creators = listOf<Int>()
    private var offset = 0
    private val limit = 20
    var total = 0

    fun getStories() = liveData(Dispatchers.IO) {
        val response = repository.getStories(offset, limit, characters, comics, events, series, creators)

        offset = response.data.offset + response.data.count
        total = response.data.total

        emit(response.data.results)
    }

    fun applyFilter(filter: Filter) {
        characters = filter.filterMap[Filter.CHARACTER]?.map { c -> c.id } ?: listOf()
        comics = filter.filterMap[Filter.COMIC]?.map { c -> c.id } ?: listOf()
        events = filter.filterMap[Filter.EVENT]?.map { e -> e.id } ?: listOf()
        series = filter.filterMap[Filter.SERIES]?.map { s -> s.id } ?: listOf()
        creators = filter.filterMap[Filter.CREATOR]?.map { c -> c.id } ?: listOf()

        offset = 0
        total = 0
    }

    @Suppress("UNCHECKED_CAST")
    class StoryViewModelFactory(
        private val repository: StoryRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return StoryViewModel(repository) as T
        }
    }
}