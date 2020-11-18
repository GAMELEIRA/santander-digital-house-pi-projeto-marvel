package com.example.marvelworld.storylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers

class StoryViewModel(
    private val repository: StoryRepository
) : ViewModel() {

    fun getStories() = liveData(Dispatchers.IO) {
        val response = repository.getStories()
        emit(response.data.results)
    }

    class StoryViewModelFactory(
        private val repository: StoryRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return StoryViewModel(repository) as T
        }
    }
}