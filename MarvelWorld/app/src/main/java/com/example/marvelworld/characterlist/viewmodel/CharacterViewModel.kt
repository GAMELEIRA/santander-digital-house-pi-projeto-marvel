package com.example.marvelworld.characterlist.viewmodel

import androidx.lifecycle.*
import com.example.marvelworld.characterlist.repository.CharacterRepository
import com.example.marvelworld.filters.models.Filter
import kotlinx.coroutines.Dispatchers

class CharacterViewModel(
    private val repository: CharacterRepository
) : ViewModel() {
    private var name: String? = null
    private var comics = listOf<Int>()
    private var events = listOf<Int>()
    private var series = listOf<Int>()
    private var offset = 0
    private val limit = 20
    var total = 0

    fun getCharacters() = liveData(Dispatchers.IO) {
        val response = repository.getCharacters(offset, limit, name, comics, series, events)

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
    class CharacterViewModelFactory(
        private val repository: CharacterRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CharacterViewModel(repository) as T
        }
    }
}