package com.example.marvelworld.storydetails.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.marvelworld.reusablecomponents.horizontallist.HorizontalListItem
import com.example.marvelworld.reusablecomponents.horizontallist.HorizontalListUtils
import com.example.marvelworld.storydetails.repository.StoryDetailsRepository
import kotlinx.coroutines.Dispatchers

@Suppress("UNCHECKED_CAST")
class StoryDetailsViewModel(
    private val repository: StoryDetailsRepository
) : ViewModel() {
    fun getStory(storyId: Int) = liveData(Dispatchers.IO) {
        val response = repository.getStory(storyId)
        emit(response.data.results[0])
    }

    fun getStoryCharacters(storyId: Int) = liveData(Dispatchers.IO) {
        val response = repository.getStoryCharacters(storyId)
        emit(response.data.results.map { character ->
            HorizontalListItem(
                character.id,
                character.name,
                HorizontalListUtils.CHARACTER
            )
        })
    }

    fun getStoryComics(storyId: Int) = liveData(Dispatchers.IO) {
        val response = repository.getStoryComics(storyId)
        emit(response.data.results.map { comic ->
            HorizontalListItem(
                comic.id,
                comic.title,
                HorizontalListUtils.COMIC
            )
        })
    }

    fun getStoryEvents(storyId: Int) = liveData(Dispatchers.IO) {
        val response = repository.getStoryEvents(storyId)
        emit(response.data.results.map { event ->
            HorizontalListItem(
                event.id,
                event.title,
                HorizontalListUtils.EVENT
            )
        })
    }

    fun getStorySeries(storyId: Int) = liveData(Dispatchers.IO) {
        val response = repository.getStorySeries(storyId)
        emit(response.data.results.map { story ->
            HorizontalListItem(
                story.id,
                story.title,
                HorizontalListUtils.SERIES
            )
        })
    }

    fun getStoryCreators(storyId: Int) = liveData(Dispatchers.IO) {
        val response = repository.getStoryCreators(storyId)
        emit(response.data.results.map { creator ->
            HorizontalListItem(
                creator.id,
                creator.fullName,
                HorizontalListUtils.CREATOR
            )
        })
    }

    class StoryDetailsViewModelFactory(
        private val repository: StoryDetailsRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return StoryDetailsViewModel(repository) as T
        }
    }
}