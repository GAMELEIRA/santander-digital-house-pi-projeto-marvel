package com.example.marvelworld.eventlist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.marvelworld.eventlist.repository.EventRepository
import kotlinx.coroutines.Dispatchers

class EventViewModel(
    private val repository: EventRepository
) : ViewModel() {

    fun getEvents() = liveData(Dispatchers.IO) {
        val response = repository.getEvents()
        emit(response.data.results)
    }

    class EventViewModelFactory(
        private val repository: EventRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return EventViewModel(repository) as T
        }
    }
}