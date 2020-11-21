package com.example.marvelworld.characterlist.viewmodel

import androidx.lifecycle.*
import com.example.marvelworld.characterlist.repository.CharacterRepository
import kotlinx.coroutines.Dispatchers

@Suppress("UNCHECKED_CAST")
class CharacterViewModel(
    private val repository: CharacterRepository
) : ViewModel() {

    fun getCharacters() = liveData(Dispatchers.IO) {
        val response = repository.getCharacters()
        emit(response.data.results)
    }

    class CharacterViewModelFactory(
        private val repository: CharacterRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CharacterViewModel(repository) as T
        }
    }
}