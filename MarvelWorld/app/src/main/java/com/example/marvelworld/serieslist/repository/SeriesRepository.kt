package com.example.marvelworld.serieslist.repository

import com.example.marvelworld.api.utils.NetworkUtils

class SeriesRepository {
    private val seriesService = NetworkUtils.getApiService(SeriesEndpoint::class.java)

    suspend fun getSeries(
        offset: Int,
        limit: Int,
        titleStartsWith: String?,
        characters: List<Int>,
        comics: List<Int>,
        events: List<Int>,
        creators: List<Int>
    ) = seriesService.getSeries(
        offset,
        limit,
        titleStartsWith,
        characters,
        comics,
        events,
        creators
    )

    suspend fun getOneSeries(seriesId: Int) = seriesService.getOneSeries(seriesId)
}