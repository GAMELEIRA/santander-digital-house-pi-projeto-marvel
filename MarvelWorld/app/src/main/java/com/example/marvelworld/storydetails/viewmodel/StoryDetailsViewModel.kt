package com.example.marvelworld.storydetails.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.marvelworld.favorite.respository.FavoriteRepository
import com.example.marvelworld.reusablecomponents.horizontallist.HorizontalListItem
import com.example.marvelworld.reusablecomponents.horizontallist.HorizontalListUtils
import com.example.marvelworld.storydetails.repository.StoryDetailsRepository
import com.example.marvelworld.util.ResourceType
import kotlinx.coroutines.Dispatchers

class StoryDetailsViewModel(
    private val storyRepository: StoryDetailsRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {
    fun getStory(storyId: Int) = liveData(Dispatchers.IO) {
        val response = storyRepository.getStory(storyId)
        val story = response.data.results[0]
        story.isFavorite = favoriteRepository.isFavorite(story.id, ResourceType.STORY)
        emit(response.data.results[0])
    }

    fun getStoryCharacters(storyId: Int) = liveData(Dispatchers.IO) {
        val response = storyRepository.getStoryCharacters(storyId)
        emit(response.data.results.map { character ->
            HorizontalListItem(
                character.id,
                character.name,
                HorizontalListUtils.CHARACTER
            )
        })
    }

    fun getStoryComics(storyId: Int) = liveData(Dispatchers.IO) {
        val response = storyRepository.getStoryComics(storyId)
        emit(response.data.results.map { comic ->
            HorizontalListItem(
                comic.id,
                comic.title,
                HorizontalListUtils.COMIC
            )
        })
    }

    fun getStoryEvents(storyId: Int) = liveData(Dispatchers.IO) {
        val response = storyRepository.getStoryEvents(storyId)
        emit(response.data.results.map { event ->
            HorizontalListItem(
                event.id,
                event.title,
                HorizontalListUtils.EVENT
            )
        })
    }

    fun getStorySeries(storyId: Int) = liveData(Dispatchers.IO) {
        val response = storyRepository.getStorySeries(storyId)
        emit(response.data.results.map { story ->
            HorizontalListItem(
                story.id,
                story.title,
                HorizontalListUtils.SERIES
            )
        })
    }

    fun getStoryCreators(storyId: Int) = liveData(Dispatchers.IO) {
        val response = storyRepository.getStoryCreators(storyId)
        emit(response.data.results.map { creator ->
            HorizontalListItem(
                creator.id,
                creator.fullName,
                HorizontalListUtils.CREATOR
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