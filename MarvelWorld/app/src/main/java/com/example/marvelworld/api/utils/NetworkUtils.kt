package com.example.marvelworld.api.utils

import com.google.gson.GsonBuilder
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

        val dateFormat = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss-SSSS").create()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(dateFormat))
            .client(okHttpClient)
            .build().create(clazz)
    }
}