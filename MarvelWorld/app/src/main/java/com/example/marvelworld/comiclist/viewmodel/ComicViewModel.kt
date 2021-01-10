package com.example.marvelworld.comiclist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.marvelworld.comiclist.repository.ComicRepository
import com.example.marvelworld.filters.models.Filter
import kotlinx.coroutines.Dispatchers

class ComicViewModel(
    private val repository: ComicRepository
) : ViewModel() {
    private var title: String? = null
    private var characters = listOf<Int>()
    private var events = listOf<Int>()
    private var series = listOf<Int>()
    private var creators = listOf<Int>()
    private var offset = 0
    private val limit = 20
    var total = 0

    fun getComics() = liveData(Dispatchers.IO) {
        val response = repository.getComics(offset, limit, title, characters, events, series, creators)

        offset = response.data.offset + response.data.count
        total = response.data.total

        emit(response.data.results)
    }

    fun applyFilter(filter: Filter) {
        title = filter.text
        characters = filter.filterMap[Filter.CHARACTER]?.map { c -> c.id } ?: listOf()
        events = filter.filterMap[Filter.EVENT]?.map { e -> e.id } ?: listOf()
        series = filter.filterMap[Filter.SERIES]?.map { s -> s.id } ?: listOf()
        creators = filter.filterMap[Filter.CREATOR]?.map { s -> s.id } ?: listOf()

        offset = 0
        total = 0
    }

    @Suppress("UNCHECKED_CAST")
    class ComicViewModelFactory(
        private val repository: ComicRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ComicViewModel(repository) as T
        }
    }
}