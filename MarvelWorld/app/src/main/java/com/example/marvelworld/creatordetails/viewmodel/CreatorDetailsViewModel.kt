package com.example.marvelworld.creatordetails.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.marvelworld.creatordetails.repository.CreatorDetailsRepository
import com.example.marvelworld.reusablecomponents.horizontallist.HorizontalListItem
import com.example.marvelworld.reusablecomponents.horizontallist.HorizontalListUtils
import kotlinx.coroutines.Dispatchers

@Suppress("UNCHECKED_CAST")
class CreatorDetailsViewModel(
    private val repository: CreatorDetailsRepository
) : ViewModel() {
    fun getCreator(creatorId: Int) = liveData(Dispatchers.IO) {
        val response = repository.getCreator(creatorId)
        emit(response.data.results[0])
    }

    fun getCreatorComics(creatorId: Int) = liveData(Dispatchers.IO) {
        val response = repository.getCreatorComics(creatorId)
        emit(response.data.results.map { comic ->
            HorizontalListItem(
                comic.id,
                comic.title,
                HorizontalListUtils.COMIC
            )
        })
    }

    fun getCreatorEvents(creatorId: Int) = liveData(Dispatchers.IO) {
        val response = repository.getCreatorEvents(creatorId)
        emit(response.data.results.map { event ->
            HorizontalListItem(
                event.id,
                event.title,
                HorizontalListUtils.EVENT
            )
        })
    }

    fun getCreatorStories(creatorId: Int) = liveData(Dispatchers.IO) {
        val response = repository.getCreatorStories(creatorId)
        emit(response.data.results.map { story ->
            HorizontalListItem(
                story.id,
                story.title,
                HorizontalListUtils.STORY
            )
        })
    }

    fun getCreatorSeries(creatorId: Int) = liveData(Dispatchers.IO) {
        val response = repository.getCreatorSeries(creatorId)
        emit(response.data.results.map { series ->
            HorizontalListItem(
                series.id,
                series.title,
                HorizontalListUtils.SERIES
            )
        })
    }

    class CreatorDetailsViewModelFactory(
        private val repository: CreatorDetailsRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CreatorDetailsViewModel(repository) as T
        }
    }
}