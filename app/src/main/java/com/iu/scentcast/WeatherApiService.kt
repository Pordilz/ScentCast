package com.iu.scentcast

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    // URL Endpoint: data/2.5/weather?q={city}&appid={key}&units=metric
    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric" // Use metric for Celsius
    ): Response<WeatherResponse>
}