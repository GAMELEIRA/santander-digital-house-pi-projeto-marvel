package com.example.marvelworld.serieslist.repository

import com.example.marvelworld.api.utils.NetworkUtils

class SeriesRepository {
    private val seriesService = NetworkUtils.getApiService(SeriesEndpoint::class.java)

    suspend fun getSeries() = seriesService.getSeries()
    suspend fun getOneSeries(seriesId: Int) = seriesService.getOneSeries(seriesId)
    suspend fun getSeriesCharacters(seriesId: Int) = seriesService.getSeriesCharacters(seriesId)
    suspend fun getSeriesComics(seriesId: Int) = seriesService.getSeriesComics(seriesId)
    suspend fun getSeriesCreators(seriesId: Int) = seriesService.getSeriesCreators(seriesId)
    suspend fun getSeriesEvents(seriesId: Int) = seriesService.getSeriesEvents(seriesId)
    suspend fun getSeriesStories(seriesId: Int) = seriesService.getSeriesStories(seriesId)
}