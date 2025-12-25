package com.iu.scentcast

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    // Create the Retrofit object once (Singleton)
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Create the API Service
    val api: WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }
}