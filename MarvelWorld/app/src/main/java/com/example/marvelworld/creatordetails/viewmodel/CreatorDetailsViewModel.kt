package com.example.marvelworld.creatordetails.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.marvelworld.creatordetails.repository.CreatorDetailsRepository
import com.example.marvelworld.favorite.respository.FavoriteRepository
import com.example.marvelworld.horizontallist.HorizontalListItem
import com.example.marvelworld.horizontallist.InfiniteScrollable
import com.example.marvelworld.util.ResourceType
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers

class CreatorDetailsViewModel(
    private val creatorRepository: CreatorDetailsRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel(), InfiniteScrollable {
    private val userId by lazy { FirebaseAuth.getInstance().currentUser!!.uid }

    override var offset = 0
    override val limit = 20
    override var total = 0

    fun getCreator(creatorId: Int) = liveData(Dispatchers.IO) {
        val response = creatorRepository.getCreator(creatorId)
        val creator = response.data.results[0]
        creator.isFavorite = favoriteRepository.isFavorite(userId, creator.id, ResourceType.CREATOR)
        emit(creator)
    }

    override fun getComics(resourceId: Int) = liveData(Dispatchers.IO) {
        val response = creatorRepository.getCreatorComics(offset, limit, resourceId)

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

    override fun getEvents(resourceId: Int) = liveData(Dispatchers.IO) {
        val response = creatorRepository.getCreatorEvents(offset, limit, resourceId)

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
        val response = creatorRepository.getCreatorSeries(offset, limit, resourceId)

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

    override fun getStories(resourceId: Int) = liveData(Dispatchers.IO) {
        val response = creatorRepository.getCreatorStories(offset, limit, resourceId)

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