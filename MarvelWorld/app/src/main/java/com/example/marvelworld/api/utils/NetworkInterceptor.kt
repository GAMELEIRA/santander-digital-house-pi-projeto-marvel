package com.example.marvelworld.api.utils

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.math.BigInteger
import java.security.MessageDigest

class NetworkInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val ts = System.currentTimeMillis().toString()
        val apiKey = PUB_API_KEY
        val hashInput = "$ts$PRIV_API_KEY$PUB_API_KEY"

        val httpUrl = originalRequest.url().newBuilder()
            .addQueryParameter(TS, ts)
            .addQueryParameter(API_KEY, apiKey)
            .addQueryParameter(HASH, hashInput.md5()).build()

        val builder = originalRequest.newBuilder().url(httpUrl)
        val newRequest = builder.build()

        return chain.proceed(newRequest)
    }

    companion object {
        private const val TS = "ts"
        private const val API_KEY = "apikey"
        private const val HASH = "hash"

        private const val PUB_API_KEY = "49aa3fb0da9504733cebe075afdae8aa"
        private const val PRIV_API_KEY = "65fdc7b2ab2d57d77cb56dbb573186ac0a2f2eb3"

        fun String.md5(): String {
            val md = MessageDigest.getInstance("MD5")
            return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
        }
    }
}