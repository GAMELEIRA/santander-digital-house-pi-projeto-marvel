package com.example.marvelworld.comicdetails.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.marvelworld.comicdetails.respository.ComicDetailsRepository
import com.example.marvelworld.reusablecomponents.horizontallist.HorizontalListItem
import com.example.marvelworld.reusablecomponents.horizontallist.HorizontalListUtils
import kotlinx.coroutines.Dispatchers

@Suppress("UNCHECKED_CAST")
class ComicDetailsViewModel(
    private val repository: ComicDetailsRepository
) : ViewModel() {
    fun getComic(comicId: Int) = liveData(Dispatchers.IO) {
        val response = repository.getComic(comicId)
        emit(response.data.results[0])
    }

    fun getComicCharacters(comicId: Int) = liveData(Dispatchers.IO) {
        val response = repository.getComicCharacters(comicId)
        emit(response.data.results.map { character ->
            HorizontalListItem(
                character.id,
                character.name,
                HorizontalListUtils.CHARACTER
            )
        })
    }

    fun getComicStories(comicId: Int) = liveData(Dispatchers.IO) {
        val response = repository.getComicStories(comicId)
        emit(response.data.results.map { story ->
            HorizontalListItem(
                story.id,
                story.title,
                HorizontalListUtils.STORY
            )
        })
    }

    fun getComicEvents(comicId: Int) = liveData(Dispatchers.IO) {
        val response = repository.getComicEvents(comicId)
        emit(response.data.results.map { event ->
            HorizontalListItem(
                event.id,
                event.title,
                HorizontalListUtils.EVENT
            )
        })
    }

    fun getComicCreators(comicId: Int) = liveData(Dispatchers.IO) {
        val response = repository.getComicCreators(comicId)
        emit(response.data.results.map { creator ->
            HorizontalListItem(
                creator.id,
                creator.fullName,
                HorizontalListUtils.CREATOR
            )
        })
    }

    class ComicDetailsViewModelFactory(
        private val repository: ComicDetailsRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ComicDetailsViewModel(repository) as T
        }
    }
}