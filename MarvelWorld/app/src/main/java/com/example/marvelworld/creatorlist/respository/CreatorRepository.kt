package com.example.marvelworld.creatorlist.respository

import com.example.marvelworld.api.utils.NetworkUtils

class CreatorRepository {
    private val creatorService = NetworkUtils.getApiService(CreatorEndpoint::class.java)

    suspend fun getCreators() = creatorService.getCreators()
}