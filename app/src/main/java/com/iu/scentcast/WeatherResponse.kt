package com.iu.scentcast

import com.google.gson.annotations.SerializedName

// The outer wrapper
data class WeatherResponse(
    @SerializedName("main") val main: MainStats,
    @SerializedName("name") val cityName: String
)

// The inner "main" block
data class MainStats(
    @SerializedName("temp") val temp: Double,
    @SerializedName("humidity") val humidity: Int
)