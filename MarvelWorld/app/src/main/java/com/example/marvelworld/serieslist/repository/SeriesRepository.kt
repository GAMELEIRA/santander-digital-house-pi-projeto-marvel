package com.example.marvelworld.serieslist.repository

import com.example.marvelworld.api.utils.NetworkUtils

class SeriesRepository {
    private val seriesService = NetworkUtils.getApiService(SeriesEndpoint::class.java)

    suspend fun getSeries() = seriesService.getSeries()
}