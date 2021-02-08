package com.example.marvelworld.favorite.viewmodel

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.marvelworld.R
import com.example.marvelworld.characterlist.views.CharacterListFragment
import com.example.marvelworld.comiclist.views.ComicListFragment
import com.example.marvelworld.creatorlist.views.CreatorListFragment
import com.example.marvelworld.eventlist.views.EventListFragment
import com.example.marvelworld.favorite.respository.FavoriteRepository
import com.example.marvelworld.serieslist.views.SeriesListFragment
import com.example.marvelworld.storylist.views.StoryListFragment
import com.example.marvelworld.util.ResourceType
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers

class FavoriteViewModel(
    private val favoriteRepository: FavoriteRepository,
    private val context: Context
) : ViewModel() {
    private val userId by lazy { FirebaseAuth.getInstance().currentUser!!.uid }

    fun getFavoriteFragments() = liveData(Dispatchers.IO) {
        val fragments = mutableMapOf<String, Fragment>()

        if (existsFavoriteCharacter()) {
            val fragment = CharacterListFragment(true)
            fragments[context.getString(R.string.characters)] = fragment
        }

        if (existsFavoriteComic()) {
            val fragment = ComicListFragment(true)
            fragments[context.getString(R.string.comics)] = fragment
        }

        if (existsFavoriteEvent()) {
            val fragment = EventListFragment(true)
            fragments[context.getString(R.string.events)] = fragment
        }

        if (existsFavoriteSeries()) {
            val fragment = SeriesListFragment(true)
            fragments[context.getString(R.string.series)] = fragment
        }

        if (existsFavoriteStory()) {
            val fragment = StoryListFragment(true)
            fragments[context.getString(R.string.stories)] = fragment
        }

        if (existsFavoriteCreator()) {
            val fragment = CreatorListFragment(true)
            fragments[context.getString(R.string.creators)] = fragment
        }

        emit(fragments)
    }

    private suspend fun existsFavoriteCharacter() =
        favoriteRepository.countFavorites(userId, ResourceType.CHARACTER) > 0

    private suspend fun existsFavoriteComic() =
        favoriteRepository.countFavorites(userId, ResourceType.COMIC) > 0

    private suspend fun existsFavoriteSeries() =
        favoriteRepository.countFavorites(userId, ResourceType.SERIES) > 0

    private suspend fun existsFavoriteEvent() =
        favoriteRepository.countFavorites(userId, ResourceType.EVENT) > 0

    private suspend fun existsFavoriteStory() =
        favoriteRepository.countFavorites(userId, ResourceType.STORY) > 0

    private suspend fun existsFavoriteCreator() =
            favoriteRepository.countFavorites(userId, ResourceType.CREATOR) > 0

    @Suppress("UNCHECKED_CAST")
    class FavoriteViewModelFactory(
        private val favoriteRepository: FavoriteRepository,
        private val context: Context
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return FavoriteViewModel(favoriteRepository, context) as T
        }
    }
}