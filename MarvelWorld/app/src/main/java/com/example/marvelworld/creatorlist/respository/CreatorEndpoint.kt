package com.example.marvelworld.creatorlist.respository

import com.example.marvelworld.api.models.DataWrapper
import com.example.marvelworld.creatorlist.models.Creator
import retrofit2.http.GET

interface CreatorEndpoint {
    @GET("/v1/public/creators")
    suspend fun getCreators(): DataWrapper<Creator>
}