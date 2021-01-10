package com.example.marvelworld.serieslist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.marvelworld.filters.models.Filter
import com.example.marvelworld.serieslist.repository.SeriesRepository
import kotlinx.coroutines.Dispatchers

class SeriesViewModel(
    private val repository: SeriesRepository
) : ViewModel() {
    private var title: String? = null
    private var characters = listOf<Int>()
    private var comics = listOf<Int>()
    private var events = listOf<Int>()
    private var creators = listOf<Int>()
    private var offset = 0
    private val limit = 20
    var total = 0

    fun getSeries() = liveData(Dispatchers.IO) {
        val response = repository.getSeries(offset, limit, title, characters, comics, events, creators)

        offset = response.data.offset + response.data.count
        total = response.data.total

        emit(response.data.results)
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
        private val repository: SeriesRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return SeriesViewModel(repository) as T
        }
    }
}