package com.mzansi.campushub

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Singleton object responsible for building and providing a single shared
 * Retrofit-backed [ApiService] instance for the whole application.
 *
 * Usage: RetrofitClient.instance.getEvents().enqueue(...)
 *
 * NOTE: Replace [BASE_URL] with your actual production backend URL before
 * shipping the app. The placeholder below points at a mock/test endpoint.
 */
object RetrofitClient {

    private const val BASE_URL = "https://4e21c76d-4ec9-44f9-862c-073a4e2a7503.mock.pstmn.io/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * The shared [ApiService] instance used across the app to make network calls.
     */
    val instance: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    /**
     * Returns the configured base URL, for diagnostic logging only (e.g.
     * confirming which host MainActivity is actually calling).
     */
    fun baseUrlForLogging(): String = BASE_URL
}