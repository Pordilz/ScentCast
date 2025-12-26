package com.iu.scentcast

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

/**
 * ViewModel for the ScentCast application.
 * Manages UI-related data and handles communication between the Repository and the UI.
 * 
 * Features:
 * 1. Database operations for fragrance collection.
 * 2. Weather-based fragrance recommendation.
 * 3. Discovery engine for new scent suggestions.
 * 4. Longevity tracker for spray times.
 */
class ScentViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FragranceRepository
    val allFragrances: LiveData<List<Fragrance>>

    // Weather and Recommendation State
    private val _currentTemp = MutableLiveData<Double>()
    val currentTemp: LiveData<Double> = _currentTemp

    private val _recommendation = MutableLiveData<Fragrance?>()
    val recommendation: LiveData<Fragrance?> = _recommendation

    // Discovery Feature State
    private val _discovery = MutableLiveData<String>()
    val discovery: LiveData<String> = _discovery

    // Longevity Feature State (Persistence using SharedPreferences)
    private val sharedPrefs = application.getSharedPreferences("ScentPrefs", Context.MODE_PRIVATE)
    private val _timeSinceSpray = MutableLiveData<String>()
    val timeSinceSpray: LiveData<String> = _timeSinceSpray

    init {
        val fragranceDao = AppDatabase.getDatabase(application).fragranceDao()
        repository = FragranceRepository(fragranceDao)
        allFragrances = repository.allFragrances.asLiveData()
        updateTimeSinceSpray() // Initialize spray timer on startup
    }

    /**
     * Adds a new fragrance to the database.
     */
    fun addFragrance(fragrance: Fragrance) = viewModelScope.launch {
        repository.insert(fragrance)
    }

    /**
     * Fetches the current weather and triggers the recommendation/discovery logic.
     */
    fun fetchWeatherAndRecommend() = viewModelScope.launch {
        val weatherResponse = repository.getWeather()
        val temp = weatherResponse?.main?.temp ?: 20.0
        _currentTemp.postValue(temp)

        // Trigger discovery engine based on the fetched temperature
        recommendNewScent(temp)
    }

    /**
     * Recommends a fragrance from the user's collection based on the current temperature.
     * 
     * @param temp The current temperature in Celsius.
     * @param collection The list of fragrances to filter from.
     */
    fun recommendScent(temp: Double, collection: List<Fragrance>) {
        if (collection.isEmpty()) {
            _recommendation.postValue(null)
            return
        }

        // Logic handled by RecommendationEngine
        val suitableScents = RecommendationEngine.filterByWeather(temp, collection)

        // Fallback: pick a random suitable scent, or any random scent if no matches
        val finalPick = suitableScents.randomOrNull() ?: collection.random()

        _recommendation.postValue(finalPick)
    }

    /**
     * Discovery Engine: Provides static suggestions for popular scents based on weather.
     */
    private fun recommendNewScent(temp: Double) {
        val suggestion = when {
            temp >= 25.0 -> "Try 'Dolce & Gabbana Light Blue' for high heat."
            temp >= 20.0 -> "Try 'Bleu de Chanel' for this warm weather."
            temp >= 10.0 -> "Try 'Terre d'Hermes' for cool, crisp air."
            else -> "Try 'Viktor&Rolf Spicebomb' for the cold."
        }
        _discovery.postValue(suggestion)
    }

    /**
     * Longevity Tracker: Logs the current time as the "spray time".
     */
    fun logSprayTime() {
        val now = System.currentTimeMillis()
        sharedPrefs.edit().putLong("last_spray_time", now).apply()
        updateTimeSinceSpray()
    }

    /**
     * Updates the UI-bound timer showing how long ago the user applied a fragrance.
     */
    private fun updateTimeSinceSpray() {
        val lastTime = sharedPrefs.getLong("last_spray_time", 0)
        if (lastTime == 0L) {
            _timeSinceSpray.postValue("Not applied yet")
            return
        }

        val diff = System.currentTimeMillis() - lastTime
        val minutes = (diff / (1000 * 60)) % 60
        val hours = (diff / (1000 * 60 * 60))

        if (hours > 6) {
            _timeSinceSpray.postValue("Scents fade after 6h. Reapply?")
        } else {
            _timeSinceSpray.postValue("Applied ${hours}h ${minutes}m ago")
        }
    }

    /**
     * Deletes a fragrance from the database.
     */
    fun deleteFragrance(fragrance: Fragrance) = viewModelScope.launch {
        repository.delete(fragrance)
    }

    /**
     * Updates a fragrance's details in the database.
     */
    fun updateFragrance(fragrance: Fragrance) = viewModelScope.launch {
        repository.update(fragrance)
    }
}