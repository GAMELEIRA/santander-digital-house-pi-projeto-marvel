package com.example.marvelworld.characterlist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.marvelworld.api.models.Image
import com.example.marvelworld.characterlist.models.Character
import com.example.marvelworld.characterlist.repository.CharacterRepository
import com.example.marvelworld.favorite.models.Favorite
import com.example.marvelworld.favorite.respository.FavoriteRepository
import com.example.marvelworld.filters.models.Filter
import com.example.marvelworld.util.ResourceType
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers

class CharacterViewModel(
    private val characterRepository: CharacterRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {
    private val userId by lazy { FirebaseAuth.getInstance().currentUser!!.uid }

    private var name: String? = null
    private var comics = listOf<Int>()
    private var events = listOf<Int>()
    private var series = listOf<Int>()
    private var offset = 0
    private val limit = 20
    var total = 0

    private var offsetFavorite = 0
    private val limitFavorite = 20
    var totalFavorite = 0

    fun getCharacters() = liveData(Dispatchers.IO) {
        val response =
            characterRepository.getCharacters(offset, limit, name, comics, series, events)

        offset = response.data.offset + response.data.count
        total = response.data.total

        response.data.results.forEach {
            it.isFavorite = favoriteRepository.isFavorite(userId, it.id, ResourceType.CHARACTER)
        }

        emit(response.data.results)
    }

    fun updateCharacters(characters: MutableList<Character?>) = liveData(Dispatchers.IO) {
        characters.forEach {
            it!!.isFavorite = favoriteRepository.isFavorite(userId, it.id, ResourceType.CHARACTER)
        }

        emit(true)
    }

    fun getFavoriteCharacters() = liveData(Dispatchers.IO) {
        val favorites = favoriteRepository.getFavorites(
            offsetFavorite,
            limitFavorite,
            userId,
            ResourceType.CHARACTER
        )
        val characters = mutableListOf<Character>()

        totalFavorite = favoriteRepository.countFavorites(userId, ResourceType.CHARACTER)
        offsetFavorite += favorites.size

        favorites.forEach {
            val character = Character(
                it.resourceId,
                it.title,
                "",
                listOf(),
                Image(it.imagePath!!, it.imageExtension!!),
                true
            )
            character.isFavorite = true
            characters.add(character)
        }

        emit(characters)
    }

    fun updateFavoriteCharacters(characters: MutableList<Character?>) = liveData(Dispatchers.IO) {
        val charactersToRemove = mutableListOf<Character>()
        characters.forEach {
            val isFavorite = favoriteRepository.isFavorite(userId, it!!.id, ResourceType.CHARACTER)
            if (!isFavorite) charactersToRemove.add(it)
        }

        emit(charactersToRemove)
    }

    fun addFavorite(resourceId: Int, title: String, imagePath: String?, imageExtension: String?) =
        liveData(Dispatchers.IO) {
            favoriteRepository.addFavorite(
                Favorite(
                    userId,
                    resourceId,
                    ResourceType.CHARACTER,
                    title,
                    imagePath,
                    imageExtension
                )
            )
            emit(true)
        }

    fun removeFavorite(resourceId: Int) = liveData(Dispatchers.IO) {
        favoriteRepository.removeFavorite(userId, resourceId, ResourceType.CHARACTER)
        emit(true)
    }

    fun isFavorite(resourceId: Int) = liveData(Dispatchers.IO) {
        val result = favoriteRepository.isFavorite(userId, resourceId, ResourceType.CHARACTER)
        emit(result)
    }

    fun applyFilter(filter: Filter) {
        name = filter.text
        comics = filter.filterMap[Filter.COMIC]?.map { c -> c.id } ?: listOf()
        events = filter.filterMap[Filter.EVENT]?.map { e -> e.id } ?: listOf()
        series = filter.filterMap[Filter.SERIES]?.map { s -> s.id } ?: listOf()

        offset = 0
        total = 0
    }

    @Suppress("UNCHECKED_CAST")
    class CharacterViewModelFactory(
        private val characterRepository: CharacterRepository,
        private val favoriteRepository: FavoriteRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CharacterViewModel(characterRepository, favoriteRepository) as T
        }
    }
}