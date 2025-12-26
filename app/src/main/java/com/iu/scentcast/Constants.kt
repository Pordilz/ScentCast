package com.iu.scentcast

/**
 * Global configuration and constant values used throughout the application.
 */
object Constants {
    /**
     * The base URL for the OpenWeatherMap API.
     */
    const val BASE_URL = "https://api.openweathermap.org/"
    
    /**
     * API key for accessing OpenWeatherMap services.
     * This is now fetched from BuildConfig for security (loaded from local.properties).
     */
    val API_KEY = BuildConfig.OPENWEATHER_API_KEY
    
    /**
     * Default city used for weather lookups.
     */
    const val MY_CITY = "Durban"
}