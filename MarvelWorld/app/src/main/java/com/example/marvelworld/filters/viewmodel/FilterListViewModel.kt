package com.example.marvelworld.filters.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.marvelworld.filters.models.Filter
import com.example.marvelworld.filters.models.FilterServiceResponse

class FilterListViewModel : ViewModel() {
    var characters = MutableLiveData<List<FilterServiceResponse>>()
    var comics = MutableLiveData<List<FilterServiceResponse>>()
    var events = MutableLiveData<List<FilterServiceResponse>>()
    var series = MutableLiveData<List<FilterServiceResponse>>()
    var creators = MutableLiveData<List<FilterServiceResponse>>()

    fun setSelectedItems(list: List<FilterServiceResponse>, type: Int) {
        when (type) {
            Filter.CHARACTER -> characters.value = list
            Filter.COMIC -> comics.value = list
            Filter.EVENT -> events.value = list
            Filter.SERIES -> series.value = list
            Filter.CREATOR -> creators.value = list
        }
    }
}