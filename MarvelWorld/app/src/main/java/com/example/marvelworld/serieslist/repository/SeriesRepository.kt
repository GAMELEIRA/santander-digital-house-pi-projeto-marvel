package com.example.marvelworld.serieslist.repository

import com.example.marvelworld.api.utils.NetworkUtils

class SeriesRepository {
    private val seriesService = NetworkUtils.getApiService(SeriesEndpoint::class.java)

    suspend fun getSeries(
        titleStartsWith: String?,
        characters: List<Int>,
        comics: List<Int>,
        events: List<Int>,
        creators: List<Int>
    ) = seriesService.getSeries(
        titleStartsWith,
        characters,
        comics,
        events,
        creators
    )
}