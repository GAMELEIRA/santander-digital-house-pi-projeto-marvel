package com.example.marvelworld.api.utils

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkUtils {
    private const val BASE_URL = "https://gateway.marvel.com/"

    fun <T> getApiService(clazz: Class<T>): T {
        val okHttpClient = OkHttpClient
            .Builder()
            .addInterceptor(NetworkInterceptor())
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build().create(clazz)
    }
}