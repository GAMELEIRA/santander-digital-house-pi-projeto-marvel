package com.example.marvelworld.characterdetails.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.marvelworld.characterdetails.repository.CharacterDetailsRepository
import com.example.marvelworld.favorite.respository.FavoriteRepository
import com.example.marvelworld.horizontallist.HorizontalListItem
import com.example.marvelworld.horizontallist.InfiniteScrollable
import com.example.marvelworld.util.ResourceType
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers

class CharacterDetailsViewModel(
    private val characterRepository: CharacterDetailsRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel(), InfiniteScrollable {
    private val userId by lazy { FirebaseAuth.getInstance().currentUser!!.uid }

    override var offset = 0
    override val limit = 20
    override var total = 0

    fun getCharacter(characterId: Int) = liveData(Dispatchers.IO) {
        val response = characterRepository.getCharacter(characterId)
        val character = response.data.results[0]
        character.isFavorite = favoriteRepository.isFavorite(userId, character.id, ResourceType.CHARACTER)
        emit(character)
    }

    override fun getComics(resourceId: Int) = liveData(Dispatchers.IO) {
        val response = characterRepository.getCharacterComics(offset, limit, resourceId)

        offset = response.data.offset + response.data.count
        total = response.data.total

        emit(response.data.results.map { comic ->
            HorizontalListItem(
                comic.id,
                comic.title,
                ResourceType.COMIC
            )
        })
    }

    override fun getStories(resourceId: Int) = liveData(Dispatchers.IO) {
        val response = characterRepository.getCharacterStories(offset, limit, resourceId)

        offset = response.data.offset + response.data.count
        total = response.data.total

        emit(response.data.results.map { story ->
            HorizontalListItem(
                story.id,
                story.title,
                ResourceType.STORY
            )
        })
    }

    override fun getEvents(resourceId: Int) = liveData(Dispatchers.IO) {
        val response = characterRepository.getCharacterEvents(offset, limit, resourceId)

        offset = response.data.offset + response.data.count
        total = response.data.total

        emit(response.data.results.map { event ->
            HorizontalListItem(
                event.id,
                event.title,
                ResourceType.EVENT
            )
        })
    }

    override fun getSeries(resourceId: Int) = liveData(Dispatchers.IO) {
        val response = characterRepository.getCharacterSeries(offset, limit, resourceId)

        offset = response.data.offset + response.data.count
        total = response.data.total

        emit(response.data.results.map { series ->
            HorizontalListItem(
                series.id,
                series.title,
                ResourceType.SERIES
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