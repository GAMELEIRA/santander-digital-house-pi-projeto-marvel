package com.example.marvelworld.comicdetails.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.marvelworld.comicdetails.respository.ComicDetailsRepository
import com.example.marvelworld.favorite.respository.FavoriteRepository
import com.example.marvelworld.reusablecomponents.horizontallist.HorizontalListItem
import com.example.marvelworld.reusablecomponents.horizontallist.HorizontalListUtils
import com.example.marvelworld.util.ResourceType
import kotlinx.coroutines.Dispatchers

class ComicDetailsViewModel(
    private val comicRepository: ComicDetailsRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {
    fun getComic(comicId: Int) = liveData(Dispatchers.IO) {
        val response = comicRepository.getComic(comicId)
        val comic = response.data.results[0]
        comic.isFavorite = favoriteRepository.isFavorite(comic.id, ResourceType.COMIC)
        emit(comic)
    }

    fun getComicCharacters(comicId: Int) = liveData(Dispatchers.IO) {
        val response = comicRepository.getComicCharacters(comicId)
        emit(response.data.results.map { character ->
            HorizontalListItem(
                character.id,
                character.name,
                HorizontalListUtils.CHARACTER
            )
        })
    }

    fun getComicStories(comicId: Int) = liveData(Dispatchers.IO) {
        val response = comicRepository.getComicStories(comicId)
        emit(response.data.results.map { story ->
            HorizontalListItem(
                story.id,
                story.title,
                HorizontalListUtils.STORY
            )
        })
    }

    fun getComicEvents(comicId: Int) = liveData(Dispatchers.IO) {
        val response = comicRepository.getComicEvents(comicId)
        emit(response.data.results.map { event ->
            HorizontalListItem(
                event.id,
                event.title,
                HorizontalListUtils.EVENT
            )
        })
    }

    fun getComicCreators(comicId: Int) = liveData(Dispatchers.IO) {
        val response = comicRepository.getComicCreators(comicId)
        emit(response.data.results.map { creator ->
            HorizontalListItem(
                creator.id,
                creator.fullName,
                HorizontalListUtils.CREATOR
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