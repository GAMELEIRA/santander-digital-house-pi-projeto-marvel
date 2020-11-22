package com.example.marvelworld.seriesdetails.respository

import com.example.marvelworld.api.utils.NetworkUtils

class SeriesDetailsRepository {
    private val seriesService = NetworkUtils.getApiService(SeriesDetailsEndpoint::class.java)

    suspend fun getOneSeries(seriesId: Int) = seriesService.getOneSeries(seriesId)
    suspend fun getSeriesCharacters(seriesId: Int) = seriesService.getSeriesCharacters(seriesId)
    suspend fun getSeriesComics(seriesId: Int) = seriesService.getSeriesComics(seriesId)
    suspend fun getSeriesCreators(seriesId: Int) = seriesService.getSeriesCreators(seriesId)
    suspend fun getSeriesEvents(seriesId: Int) = seriesService.getSeriesEvents(seriesId)
    suspend fun getSeriesStories(seriesId: Int) = seriesService.getSeriesStories(seriesId)
}