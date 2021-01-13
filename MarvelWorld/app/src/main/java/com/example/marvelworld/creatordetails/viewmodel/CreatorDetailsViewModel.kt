package com.example.marvelworld.creatordetails.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.marvelworld.creatordetails.repository.CreatorDetailsRepository
import com.example.marvelworld.favorite.respository.FavoriteRepository
import com.example.marvelworld.reusablecomponents.horizontallist.HorizontalListItem
import com.example.marvelworld.reusablecomponents.horizontallist.HorizontalListUtils
import com.example.marvelworld.util.ResourceType
import kotlinx.coroutines.Dispatchers

class CreatorDetailsViewModel(
    private val creatorRepository: CreatorDetailsRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {
    fun getCreator(creatorId: Int) = liveData(Dispatchers.IO) {
        val response = creatorRepository.getCreator(creatorId)
        val creator = response.data.results[0]
        creator.isFavorite = favoriteRepository.isFavorite(creator.id, ResourceType.CREATOR)
        emit(creator)
    }

    fun getCreatorComics(creatorId: Int) = liveData(Dispatchers.IO) {
        val response = creatorRepository.getCreatorComics(creatorId)
        emit(response.data.results.map { comic ->
            HorizontalListItem(
                comic.id,
                comic.title,
                HorizontalListUtils.COMIC
            )
        })
    }

    fun getCreatorEvents(creatorId: Int) = liveData(Dispatchers.IO) {
        val response = creatorRepository.getCreatorEvents(creatorId)
        emit(response.data.results.map { event ->
            HorizontalListItem(
                event.id,
                event.title,
                HorizontalListUtils.EVENT
            )
        })
    }

    fun getCreatorStories(creatorId: Int) = liveData(Dispatchers.IO) {
        val response = creatorRepository.getCreatorStories(creatorId)
        emit(response.data.results.map { story ->
            HorizontalListItem(
                story.id,
                story.title,
                HorizontalListUtils.STORY
            )
        })
    }

    fun getCreatorSeries(creatorId: Int) = liveData(Dispatchers.IO) {
        val response = creatorRepository.getCreatorSeries(creatorId)
        emit(response.data.results.map { series ->
            HorizontalListItem(
                series.id,
                series.title,
                HorizontalListUtils.SERIES
            )
        })
    }

    @Suppress("UNCHECKED_CAST")
    class CreatorDetailsViewModelFactory(
        private val creatorRepository: CreatorDetailsRepository,
        private val favoriteRepository: FavoriteRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CreatorDetailsViewModel(creatorRepository, favoriteRepository) as T
        }
    }
}