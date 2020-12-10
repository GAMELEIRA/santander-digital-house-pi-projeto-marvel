package com.example.marvelworld.filters.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.marvelworld.filters.models.Filter
import com.example.marvelworld.filters.repository.FilterRepository
import com.example.marvelworld.filters.models.FilterServiceResponse
import kotlinx.coroutines.Dispatchers

class FilterViewModel(
    private val repository: FilterRepository
) : ViewModel() {

    fun getSuggestions(suggestType: Int, text: String): LiveData<List<FilterServiceResponse>> {
        return when(suggestType) {
            Filter.CHARACTER -> getCharacters(text)
            Filter.COMIC -> getComics(text)
            Filter.EVENT -> getEvents(text)
            Filter.SERIES -> getSeries(text)
            Filter.CREATOR -> getCreators(text)
            else -> liveData {
                emit(listOf<FilterServiceResponse>())
            }
        }
    }

    private fun getCharacters(name: String) = liveData(Dispatchers.Main) {
        val response = repository.getCharacters(name)
        emit(response.data.results)
    }

    private fun getComics(title: String) = liveData(Dispatchers.Main) {
        val response = repository.getComics(title)
        emit(response.data.results)
    }

    private fun getEvents(name: String) = liveData(Dispatchers.Main) {
        val response = repository.getEvents(name)
        emit(response.data.results)
    }

    private fun getSeries(title: String) = liveData(Dispatchers.Main) {
        val response = repository.getSeries(title)
        emit(response.data.results)
    }

    private fun getCreators(name: String) = liveData(Dispatchers.Main) {
        val response = repository.getCreators(name)
        emit(response.data.results)
    }

    @Suppress("UNCHECKED_CAST")
    class FilterViewModelFactory(
        private val repository: FilterRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return FilterViewModel(repository) as T
        }
    }
}