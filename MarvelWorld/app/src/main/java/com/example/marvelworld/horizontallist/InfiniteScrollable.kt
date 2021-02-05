package com.example.marvelworld.horizontallist

import androidx.lifecycle.liveData
import com.example.marvelworld.horizontallist.HorizontalListItem
import kotlinx.coroutines.Dispatchers

interface InfiniteScrollable {
    var offset: Int
    val limit: Int
    var total: Int

    fun getCharacters(resourceId: Int) = liveData(Dispatchers.IO) {
        emit(listOf<HorizontalListItem>())
    }

    fun getComics(resourceId: Int) = liveData(Dispatchers.IO) {
        emit(listOf<HorizontalListItem>())
    }

    fun getCreators(resourceId: Int) = liveData(Dispatchers.IO) {
        emit(listOf<HorizontalListItem>())
    }

    fun getEvents(resourceId: Int) = liveData(Dispatchers.IO) {
        emit(listOf<HorizontalListItem>())
    }

    fun getSeries(resourceId: Int) = liveData(Dispatchers.IO) {
        emit(listOf<HorizontalListItem>())
    }

    fun getStories(resourceId: Int) = liveData(Dispatchers.IO) {
        emit(listOf<HorizontalListItem>())
    }
}