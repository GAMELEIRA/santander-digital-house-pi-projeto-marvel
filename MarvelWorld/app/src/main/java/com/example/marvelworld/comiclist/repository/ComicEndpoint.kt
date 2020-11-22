package com.example.marvelworld.comiclist.repository

import com.example.marvelworld.api.models.DataWrapper
import com.example.marvelworld.comiclist.models.Comic
import retrofit2.http.GET

interface ComicEndpoint {
    @GET("/v1/public/comics")
    suspend fun getComics(): DataWrapper<Comic>
}