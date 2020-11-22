package com.example.marvelworld.creatordetails.repository

import com.example.marvelworld.api.utils.NetworkUtils

class CreatorDetailsRepository {
    private val creatorService = NetworkUtils.getApiService(CreatorDetailsEndpoint::class.java)

    suspend fun getCreator(creatorId: Int) = creatorService.getCreator(creatorId)
    suspend fun getCreatorComics(creatorId: Int) = creatorService.getCreatorComics(creatorId)
    suspend fun getCreatorEvents(creatorId: Int) = creatorService.getCreatorEvents(creatorId)
    suspend fun getCreatorSeries(creatorId: Int) = creatorService.getCreatorSeries(creatorId)
    suspend fun getCreatorStories(creatorId: Int) = creatorService.getCreatorStories(creatorId)
}