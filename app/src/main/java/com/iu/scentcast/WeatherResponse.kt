package com.iu.scentcast

import com.google.gson.annotations.SerializedName

/**
 * Data model for the OpenWeatherMap API response.
 * 
 * @property main Contains the primary weather stats like temperature and humidity.
 * @property cityName The name of the city returned by the API.
 */
data class WeatherResponse(
    @SerializedName("main") val main: MainStats,
    @SerializedName("name") val cityName: String
)

/**
 * Nested data model for the "main" object in the weather API response.
 * 
 * @property temp The current temperature (in metric units by default).
 * @property humidity The current humidity percentage.
 */
data class MainStats(
    @SerializedName("temp") val temp: Double,
    @SerializedName("humidity") val humidity: Int
)