package com.example.marvelworld.creatordetails.repository

import com.example.marvelworld.api.utils.NetworkUtils

class CreatorDetailsRepository {
    private val creatorService = NetworkUtils.getApiService(CreatorDetailsEndpoint::class.java)

    suspend fun getCreator(creatorId: Int) = creatorService.getCreator(creatorId)

    suspend fun getCreatorComics(offset: Int, limit: Int, creatorId: Int) =
        creatorService.getCreatorComics(creatorId, offset, limit)

    suspend fun getCreatorEvents(offset: Int, limit: Int, creatorId: Int) =
        creatorService.getCreatorEvents(creatorId, offset, limit)

    suspend fun getCreatorSeries(offset: Int, limit: Int, creatorId: Int) =
        creatorService.getCreatorSeries(creatorId, offset, limit)

    suspend fun getCreatorStories(offset: Int, limit: Int, creatorId: Int) =
        creatorService.getCreatorStories(creatorId, offset, limit)
}