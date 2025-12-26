package com.iu.scentcast

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Singleton object that provides and manages the Retrofit instance for network operations.
 */
object RetrofitInstance {

    /**
     * Lazily initialized Retrofit object configured with the base URL and Gson converter.
     */
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * Lazily initialized API service interface for weather-related network requests.
     */
    val api: WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }
}