package com.example.marvelworld.storydetails.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.marvelworld.favorite.respository.FavoriteRepository
import com.example.marvelworld.horizontallist.HorizontalListItem
import com.example.marvelworld.storydetails.repository.StoryDetailsRepository
import com.example.marvelworld.util.InfiniteScrollable
import com.example.marvelworld.util.ResourceType
import kotlinx.coroutines.Dispatchers

class StoryDetailsViewModel(
    private val storyRepository: StoryDetailsRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel(), InfiniteScrollable {

    override var offset = 0
    override val limit = 20
    override var total = 0

    fun getStory(storyId: Int) = liveData(Dispatchers.IO) {
        val response = storyRepository.getStory(storyId)
        val story = response.data.results[0]
        story.isFavorite = favoriteRepository.isFavorite(story.id, ResourceType.STORY)
        emit(response.data.results[0])
    }

    override fun getCharacters(resourceId: Int) = liveData(Dispatchers.IO) {
        val response = storyRepository.getStoryCharacters(offset, limit, resourceId)
        emit(response.data.results.map { character ->
            HorizontalListItem(
                character.id,
                character.name,
                ResourceType.CHARACTER
            )
        })
    }

    override fun getComics(resourceId: Int) = liveData(Dispatchers.IO) {
        val response = storyRepository.getStoryComics(offset, limit, resourceId)
        emit(response.data.results.map { comic ->
            HorizontalListItem(
                comic.id,
                comic.title,
                ResourceType.COMIC
            )
        })
    }

    override fun getEvents(resourceId: Int) = liveData(Dispatchers.IO) {
        val response = storyRepository.getStoryEvents(offset, limit, resourceId)
        emit(response.data.results.map { event ->
            HorizontalListItem(
                event.id,
                event.title,
                ResourceType.EVENT
            )
        })
    }

    override fun getSeries(resourceId: Int) = liveData(Dispatchers.IO) {
        val response = storyRepository.getStorySeries(offset, limit, resourceId)
        emit(response.data.results.map { story ->
            HorizontalListItem(
                story.id,
                story.title,
                ResourceType.SERIES
            )
        })
    }

    override fun getCreators(resourceId: Int) = liveData(Dispatchers.IO) {
        val response = storyRepository.getStoryCreators(offset, limit, resourceId)
        emit(response.data.results.map { creator ->
            HorizontalListItem(
                creator.id,
                creator.fullName,
                ResourceType.CREATOR
            )
        })
    }

    @Suppress("UNCHECKED_CAST")
    class StoryDetailsViewModelFactory(
        private val storyRepository: StoryDetailsRepository,
        private val favoriteRepository: FavoriteRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return StoryDetailsViewModel(storyRepository, favoriteRepository) as T
        }
    }
}