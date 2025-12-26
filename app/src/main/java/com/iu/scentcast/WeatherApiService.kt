package com.iu.scentcast

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit interface defining the endpoints for the OpenWeatherMap API.
 */
interface WeatherApiService {
    /**
     * Fetches the current weather for a specific city.
     * 
     * @param cityName The name of the city (e.g., "Durban").
     * @param apiKey The API key for authentication.
     * @param units The units of measurement (default is "metric" for Celsius).
     * @return A [Response] containing the [WeatherResponse] data.
     */
    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): Response<WeatherResponse>
}