package com.example.marvelworld.myprofile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.marvelworld.myprofile.repository.MyProfileRepository
import kotlinx.coroutines.Dispatchers

class MyProfileViewModel(
    private val myProfileRepository: MyProfileRepository
) : ViewModel() {

    fun removeFavorites(userId: String) = liveData(Dispatchers.IO) {
        myProfileRepository.removeFavorites(userId)
        emit(true)
    }

    @Suppress("UNCHECKED_CAST")
    class MyProfileViewModelFactory(
        private val myProfileRepository: MyProfileRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MyProfileViewModel(myProfileRepository) as T
        }
    }
}