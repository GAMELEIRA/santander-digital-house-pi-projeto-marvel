package com.example.marvelworld.characterdetails.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.marvelworld.characterdetails.repository.CharacterDetailsRepository
import com.example.marvelworld.favorite.respository.FavoriteRepository
import com.example.marvelworld.reusablecomponents.horizontallist.HorizontalListItem
import com.example.marvelworld.reusablecomponents.horizontallist.HorizontalListUtils
import com.example.marvelworld.util.ResourceType
import kotlinx.coroutines.Dispatchers

class CharacterDetailsViewModel(
    private val characterRepository: CharacterDetailsRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {
    fun getCharacter(characterId: Int) = liveData(Dispatchers.IO) {
        val response = characterRepository.getCharacter(characterId)
        val character = response.data.results[0]
        character.isFavorite = favoriteRepository.isFavorite(character.id, ResourceType.CHARACTER)
        emit(character)
    }

    fun getCharacterComics(characterId: Int) = liveData(Dispatchers.IO) {
        val response = characterRepository.getCharacterComics(characterId)
        emit(response.data.results.map { comic ->
            HorizontalListItem(
                comic.id,
                comic.title,
                HorizontalListUtils.COMIC
            )
        })
    }

    fun getCharacterStories(characterId: Int) = liveData(Dispatchers.IO) {
        val response = characterRepository.getCharacterStories(characterId)
        emit(response.data.results.map { story ->
            HorizontalListItem(
                story.id,
                story.title,
                HorizontalListUtils.STORY
            )
        })
    }

    fun getCharacterEvents(characterId: Int) = liveData(Dispatchers.IO) {
        val response = characterRepository.getCharacterEvents(characterId)
        emit(response.data.results.map { event ->
            HorizontalListItem(
                event.id,
                event.title,
                HorizontalListUtils.EVENT
            )
        })
    }

    fun getCharacterSeries(characterId: Int) = liveData(Dispatchers.IO) {
        val response = characterRepository.getCharacterSeries(characterId)
        emit(response.data.results.map { series ->
            HorizontalListItem(
                series.id,
                series.title,
                HorizontalListUtils.SERIES
            )
        })
    }

    @Suppress("UNCHECKED_CAST")
    class CharacterDetailsViewModelFactory(
        private val characterRepository: CharacterDetailsRepository,
        private val favoriteRepository: FavoriteRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CharacterDetailsViewModel(characterRepository, favoriteRepository) as T
        }
    }
}