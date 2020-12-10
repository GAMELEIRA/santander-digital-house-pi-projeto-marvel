package com.example.marvelworld.filters.repository

import com.example.marvelworld.api.utils.NetworkUtils

class FilterRepository {
    private val filterService = NetworkUtils.getApiService(FilterEndpoint::class.java)

    suspend fun getCharacters(name: String) = filterService.getCharacters(name)
    suspend fun getComics(title: String) = filterService.getComics(title)
    suspend fun getEvents(name: String) = filterService.getEvents(name)
    suspend fun getSeries(title: String) = filterService.getSeries(title)
    suspend fun getCreators(name: String) = filterService.getCreators(name)
}