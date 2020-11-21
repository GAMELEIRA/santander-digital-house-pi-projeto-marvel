package com.example.marvelworld.characterdetails.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.marvelworld.characterdetails.repository.CharacterDetailsRepository
import com.example.marvelworld.reusablecomponents.HorizontalListItem
import kotlinx.coroutines.Dispatchers

@Suppress("UNCHECKED_CAST")
class CharacterDetailsViewModel(
    private val repository: CharacterDetailsRepository
) : ViewModel() {
    fun getCharacter(characterId: Int) = liveData(Dispatchers.IO) {
        val response = repository.getCharacter(characterId)
        emit(response.data.results[0])
    }

    fun getCharacterComics(characterId: Int) = liveData(Dispatchers.IO) {
        val response = repository.getCharacterComics(characterId)
        emit(response.data.results.map { comic -> HorizontalListItem(comic.title) })
    }

    fun getCharacterEvents(characterId: Int) = liveData(Dispatchers.IO) {
        val response = repository.getCharacterEvents(characterId)
        emit(response.data.results.map { event -> HorizontalListItem(event.title) })
    }

    fun getCharacterSeries(characterId: Int) = liveData(Dispatchers.IO) {
        val response = repository.getCharacterSeries(characterId)
        emit(response.data.results.map { series -> HorizontalListItem(series.title) })
    }

    fun getCharacterStories(characterId: Int) = liveData(Dispatchers.IO) {
        val response = repository.getCharacterStories(characterId)
        emit(response.data.results.map { story -> HorizontalListItem(story.title) })
    }

    class CharacterDetailsViewModelFactory(
        private val repository: CharacterDetailsRepository
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CharacterDetailsViewModel(repository) as T
        }
    }
}