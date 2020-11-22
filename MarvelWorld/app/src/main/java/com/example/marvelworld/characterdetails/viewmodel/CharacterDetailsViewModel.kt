package com.example.marvelworld.characterdetails.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.marvelworld.characterdetails.repository.CharacterDetailsRepository
import com.example.marvelworld.reusablecomponents.horizontallist.HorizontalListItem
import com.example.marvelworld.reusablecomponents.horizontallist.HorizontalListUtils
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
        emit(response.data.results.map { comic ->
            HorizontalListItem(
                comic.id,
                comic.title,
                HorizontalListUtils.COMIC
            )
        })
    }

    fun getCharacterStories(characterId: Int) = liveData(Dispatchers.IO) {
        val response = repository.getCharacterStories(characterId)
        emit(response.data.results.map { story ->
            HorizontalListItem(
                story.id,
                story.title,
                HorizontalListUtils.STORY
            )
        })
    }

    fun getCharacterEvents(characterId: Int) = liveData(Dispatchers.IO) {
        val response = repository.getCharacterEvents(characterId)
        emit(response.data.results.map { event ->
            HorizontalListItem(
                event.id,
                event.title,
                HorizontalListUtils.EVENT
            )
        })
    }

    fun getCharacterSeries(characterId: Int) = liveData(Dispatchers.IO) {
        val response = repository.getCharacterSeries(characterId)
        emit(response.data.results.map { series ->
            HorizontalListItem(
                series.id,
                series.title,
                HorizontalListUtils.SERIES
            )
        })
    }

    class CharacterDetailsViewModelFactory(
        private val repository: CharacterDetailsRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CharacterDetailsViewModel(repository) as T
        }
    }
}