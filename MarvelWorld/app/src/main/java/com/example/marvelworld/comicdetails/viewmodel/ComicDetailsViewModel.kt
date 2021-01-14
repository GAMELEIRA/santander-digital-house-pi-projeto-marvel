package com.example.marvelworld.comicdetails.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.marvelworld.comicdetails.respository.ComicDetailsRepository
import com.example.marvelworld.favorite.respository.FavoriteRepository
import com.example.marvelworld.horizontallist.HorizontalListItem
import com.example.marvelworld.util.InfiniteScrollable
import com.example.marvelworld.util.ResourceType
import kotlinx.coroutines.Dispatchers

class ComicDetailsViewModel(
    private val comicRepository: ComicDetailsRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel(), InfiniteScrollable {

    override var offset = 0
    override val limit = 20
    override var total = 0

    fun getComic(comicId: Int) = liveData(Dispatchers.IO) {
        val response = comicRepository.getComic(comicId)
        val comic = response.data.results[0]
        comic.isFavorite = favoriteRepository.isFavorite(comic.id, ResourceType.COMIC)
        emit(comic)
    }

    override fun getCharacters(resourceId: Int) = liveData(Dispatchers.IO) {
        val response = comicRepository.getComicCharacters(offset, limit, resourceId)

        offset = response.data.offset + response.data.count
        total = response.data.total

        emit(response.data.results.map { character ->
            HorizontalListItem(
                character.id,
                character.name,
                ResourceType.CHARACTER
            )
        })
    }

    override fun getStories(resourceId: Int) = liveData(Dispatchers.IO) {
        val response = comicRepository.getComicStories(offset, limit, resourceId)

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
        val response = comicRepository.getComicEvents(offset, limit, resourceId)

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

    override fun getCreators(resourceId: Int) = liveData(Dispatchers.IO) {
        val response = comicRepository.getComicCreators(offset, limit, resourceId)

        offset = response.data.offset + response.data.count
        total = response.data.total

        emit(response.data.results.map { creator ->
            HorizontalListItem(
                creator.id,
                creator.fullName,
                ResourceType.CREATOR
            )
        })
    }

    @Suppress("UNCHECKED_CAST")
    class ComicDetailsViewModelFactory(
        private val comicRepository: ComicDetailsRepository,
        private val favoriteRepository: FavoriteRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ComicDetailsViewModel(comicRepository, favoriteRepository) as T
        }
    }
}