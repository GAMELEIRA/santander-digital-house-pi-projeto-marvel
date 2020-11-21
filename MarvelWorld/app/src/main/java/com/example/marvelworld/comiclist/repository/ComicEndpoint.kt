package com.example.marvelworld.comiclist.repository

import com.example.marvelworld.api.models.DataWrapper
import com.example.marvelworld.comiclist.models.Comic
import retrofit2.http.GET
import retrofit2.http.Path

interface ComicEndpoint {
    @GET("/v1/public/comics")
    suspend fun getComics(): DataWrapper<Comic>

    @GET("/v1/public/comics/{comicId}")
    suspend fun getComic(@Path("comicId") comicId: Int): DataWrapper<Comic>

    @GET("/v1/public/comics/{comicId}/characters")
    suspend fun getComicCharacters(@Path("comicId") comicId: Int): DataWrapper<Comic>

    @GET("/v1/public/comics/{comicId}/creators")
    suspend fun getComicCreators(@Path("comicId") comicId: Int): DataWrapper<Comic>

    @GET("/v1/public/comics/{comicId}/events")
    suspend fun getComicEvents(@Path("comicId") comicId: Int): DataWrapper<Comic>

    @GET("/v1/public/comics/{comicId}/stories")
    suspend fun getComicStories(@Path("comicId") comicId: Int): DataWrapper<Comic>
}