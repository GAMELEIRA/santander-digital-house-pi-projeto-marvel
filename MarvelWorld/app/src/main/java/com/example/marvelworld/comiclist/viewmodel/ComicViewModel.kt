package com.example.marvelworld.comiclist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.marvelworld.comiclist.repository.ComicRepository
import kotlinx.coroutines.Dispatchers

@Suppress("UNCHECKED_CAST")
class ComicViewModel(
    private val repository: ComicRepository
) : ViewModel() {

    fun getComics() = liveData(Dispatchers.IO) {
        val response = repository.getComics()
        emit(response.data.results)
    }

    class ComicViewModelFactory(
        private val repository: ComicRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ComicViewModel(repository) as T
        }
    }
}