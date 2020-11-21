package com.example.marvelworld.creatorlist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.marvelworld.creatorlist.respository.CreatorRepository
import kotlinx.coroutines.Dispatchers

class CreatorViewModel (
    private val repository: CreatorRepository
) : ViewModel() {

    fun getCreators() = liveData(Dispatchers.IO) {
        val response = repository.getCreators()
        emit(response.data.results)
    }

    class CreatorViewModelFactory(
        private val repository: CreatorRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CreatorViewModel(repository) as T
        }
    }
}