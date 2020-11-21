package com.example.marvelworld.creatorlist.respository

import com.example.marvelworld.api.utils.NetworkUtils

class CreatorRepository {
    private val creatorService = NetworkUtils.getApiService(CreatorEndpoint::class.java)

    suspend fun getCreators() = creatorService.getCreators()
    suspend fun getCreator(creatorId: Int) = creatorService.getCreator(creatorId)
    suspend fun getCreatorComics(creatorId: Int) = creatorService.getCreatorComics(creatorId)
    suspend fun getCreatorEvents(creatorId: Int) = creatorService.getCreatorEvents(creatorId)
    suspend fun getCreatorSeries(creatorId: Int) = creatorService.getCreatorSeries(creatorId)
    suspend fun getCreatorStories(creatorId: Int) = creatorService.getCreatorStories(creatorId)
}