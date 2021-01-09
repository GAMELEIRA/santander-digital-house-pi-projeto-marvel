package com.example.marvelworld.creatorlist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.marvelworld.creatorlist.respository.CreatorRepository
import com.example.marvelworld.filters.models.Filter
import kotlinx.coroutines.Dispatchers

class CreatorViewModel(
    private val repository: CreatorRepository
) : ViewModel() {
    private var name: String? = null
    private var comics = listOf<Int>()
    private var events = listOf<Int>()
    private var series = listOf<Int>()
    private var offset = 0
    private val limit = 20
    var total = 0

    fun getCreators() = liveData(Dispatchers.IO) {
        val response = repository.getCreators(offset, limit, name, comics, events, series)

        offset = response.data.offset + response.data.count
        total = response.data.total

        emit(response.data.results)
    }

    fun applyFilter(filter: Filter) {
        name = filter.text
        comics = filter.filterMap[Filter.COMIC]?.map { c -> c.id } ?: listOf()
        events = filter.filterMap[Filter.EVENT]?.map { e -> e.id } ?: listOf()
        series = filter.filterMap[Filter.SERIES]?.map { s -> s.id } ?: listOf()

        offset = 0
        total = 0
    }

    @Suppress("UNCHECKED_CAST")
    class CreatorViewModelFactory(
        private val repository: CreatorRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CreatorViewModel(repository) as T
        }
    }
}