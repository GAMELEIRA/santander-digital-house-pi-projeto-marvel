package com.example.marvelworld.seriesdetails.respository

import com.example.marvelworld.api.utils.NetworkUtils

class SeriesDetailsRepository {
    private val seriesService = NetworkUtils.getApiService(SeriesDetailsEndpoint::class.java)

    suspend fun getOneSeries(seriesId: Int) = seriesService.getOneSeries(seriesId)

    suspend fun getSeriesCharacters(offset: Int, limit: Int, seriesId: Int) =
        seriesService.getSeriesCharacters(seriesId, offset, limit)

    suspend fun getSeriesComics(offset: Int, limit: Int, seriesId: Int) =
        seriesService.getSeriesComics(seriesId, offset, limit)

    suspend fun getSeriesCreators(offset: Int, limit: Int, seriesId: Int) =
        seriesService.getSeriesCreators(seriesId, offset, limit)

    suspend fun getSeriesEvents(offset: Int, limit: Int, seriesId: Int) =
        seriesService.getSeriesEvents(seriesId, offset, limit)

    suspend fun getSeriesStories(offset: Int, limit: Int, seriesId: Int) =
        seriesService.getSeriesStories(seriesId, offset, limit)
}