package com.example.marvelworld.filters.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.marvelworld.filters.repository.FiltersRepository
import kotlinx.coroutines.Dispatchers

class FiltersViewModel(
    private val repository: FiltersRepository
) : ViewModel() {

    fun getCharacters(name: String) = liveData(Dispatchers.Main) {
        val response = repository.getCharacters(name)
        emit(response.data.results)
    }

    @Suppress("UNCHECKED_CAST")
    class FiltersViewModelFactory(
        private val repository: FiltersRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return FiltersViewModel(repository) as T
        }
    }
}