package com.example.marvelworld.storylist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.marvelworld.favorite.models.Favorite
import com.example.marvelworld.favorite.respository.FavoriteRepository
import com.example.marvelworld.filters.models.Filter
import com.example.marvelworld.storylist.models.Story
import com.example.marvelworld.storylist.repository.StoryRepository
import com.example.marvelworld.util.ResourceType
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers

class StoryViewModel(
    private val storyRepository: StoryRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {
    private val userId by lazy { FirebaseAuth.getInstance().currentUser!!.uid }

    private var characters = listOf<Int>()
    private var comics = listOf<Int>()
    private var events = listOf<Int>()
    private var series = listOf<Int>()
    private var creators = listOf<Int>()
    private var offset = 0
    private val limit = 20
    var total = 0

    private var offsetFavorite = 0
    private val limitFavorite = 20
    var totalFavorite = 0

    fun getStories() = liveData(Dispatchers.IO) {
        val response =
            storyRepository.getStories(offset, limit, characters, comics, events, series, creators)

        offset = response.data.offset + response.data.count
        total = response.data.total

        response.data.results.forEach {
            it.isFavorite = favoriteRepository.isFavorite(userId, it.id, ResourceType.STORY)
        }

        emit(response.data.results)
    }

    fun updateStories(stories: MutableList<Story>) = liveData(Dispatchers.IO) {
        stories.forEach {
            it.isFavorite = favoriteRepository.isFavorite(userId, it.id, ResourceType.STORY)
        }

        emit(true)
    }

    fun getFavoriteStories() = liveData(Dispatchers.IO) {
        val favorites = favoriteRepository.getFavorites(
            offsetFavorite,
            limitFavorite,
            userId,
            ResourceType.STORY
        )
        val stories = mutableListOf<Story>()

        totalFavorite = favoriteRepository.countFavorites(userId, ResourceType.COMIC)
        offsetFavorite += favorites.size

        favorites.forEach {
            val story = Story(it.resourceId, it.title, "", null,true)
            story.isFavorite = true
            stories.add(story)
        }

        emit(stories)
    }

    fun updateFavoriteStories(stories: MutableList<Story>) = liveData(Dispatchers.IO) {
        val storiesToRemove = mutableListOf<Story>()
        stories.forEach {
            val isFavorite = favoriteRepository.isFavorite(userId, it.id, ResourceType.STORY)
            if (!isFavorite) storiesToRemove.add(it)
        }
        emit(storiesToRemove)
    }

    fun addFavorite(resourceId: Int, title: String) = liveData(Dispatchers.IO) {
        favoriteRepository.addFavorite(
            Favorite(
                userId,
                resourceId,
                ResourceType.STORY,
                title,
                null,
                null
            )
        )
        emit(true)
    }

    fun removeFavorite(resourceId: Int) = liveData(Dispatchers.IO) {
        favoriteRepository.removeFavorite(userId, resourceId, ResourceType.STORY)
        emit(true)
    }

    fun isFavorite(resourceId: Int) = liveData(Dispatchers.IO) {
        val result = favoriteRepository.isFavorite(userId, resourceId, ResourceType.STORY)
        emit(result)
    }

    fun applyFilter(filter: Filter) {
        characters = filter.filterMap[Filter.CHARACTER]?.map { c -> c.id } ?: listOf()
        comics = filter.filterMap[Filter.COMIC]?.map { c -> c.id } ?: listOf()
        events = filter.filterMap[Filter.EVENT]?.map { e -> e.id } ?: listOf()
        series = filter.filterMap[Filter.SERIES]?.map { s -> s.id } ?: listOf()
        creators = filter.filterMap[Filter.CREATOR]?.map { c -> c.id } ?: listOf()

        offset = 0
        total = 0
    }

    @Suppress("UNCHECKED_CAST")
    class StoryViewModelFactory(
        private val storyRepository: StoryRepository,
        private val favoriteRepository: FavoriteRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return StoryViewModel(storyRepository, favoriteRepository) as T
        }
    }
}