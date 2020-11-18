package com.example.marvelworld.serieslist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.marvelworld.serieslist.repository.SeriesRepository
import kotlinx.coroutines.Dispatchers

@Suppress("UNCHECKED_CAST")
class SeriesViewModel(
    private val repository: SeriesRepository
) : ViewModel() {

    fun getSeries() = liveData(Dispatchers.IO) {
        val response = repository.getSeries()
        emit(response.data.results)
    }

    class SeriesViewModelFactory(
        private val repository: SeriesRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return SeriesViewModel(repository) as T
        }
    }
}