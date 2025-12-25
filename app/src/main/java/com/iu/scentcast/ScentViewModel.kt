package com.iu.scentcast

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ScentViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FragranceRepository
    val allFragrances: LiveData<List<Fragrance>>

    // LiveData
    private val _currentTemp = MutableLiveData<Double>()
    val currentTemp: LiveData<Double> = _currentTemp

    private val _recommendation = MutableLiveData<Fragrance?>()
    val recommendation: LiveData<Fragrance?> = _recommendation

    // NEW: Discovery Feature
    private val _discovery = MutableLiveData<String>()
    val discovery: LiveData<String> = _discovery

    // NEW: Longevity Feature
    private val sharedPrefs = application.getSharedPreferences("ScentPrefs", Context.MODE_PRIVATE)
    private val _timeSinceSpray = MutableLiveData<String>()
    val timeSinceSpray: LiveData<String> = _timeSinceSpray

    init {
        val fragranceDao = AppDatabase.getDatabase(application).fragranceDao()
        repository = FragranceRepository(fragranceDao)
        allFragrances = repository.allFragrances.asLiveData()
        updateTimeSinceSpray() // Check time on startup
    }

    fun addFragrance(fragrance: Fragrance) = viewModelScope.launch {
        repository.insert(fragrance)
    }

    fun fetchWeatherAndRecommend() = viewModelScope.launch {
        val weatherResponse = repository.getWeather()
        val temp = weatherResponse?.main?.temp ?: 20.0
        _currentTemp.postValue(temp)

        // Trigger discovery based on this temp
        recommendNewScent(temp)
    }

    // --- FEATURE 1: SMART LOGIC ALGORITHM ---
    fun recommendScent(temp: Double, collection: List<Fragrance>) {
        if (collection.isEmpty()) return

        // Granular Logic
        val suitableScents = collection.filter { f ->
            val notes = f.notes.lowercase()
            val season = f.season.lowercase()

            when {
                // HOT (> 25): Needs Citrus, Aquatic, Green
                temp >= 25.0 -> season == "summer" || notes.contains("citrus") || notes.contains("water") || notes.contains("fresh")

                // WARM (20 - 24): Floral, Fruity, Aromatic
                temp >= 20.0 -> season == "summer" || season == "all year" || notes.contains("floral") || notes.contains("fruit")

                // COOL (10 - 19): Woody, Spicy, Vetiver
                temp >= 10.0 -> season == "winter" || season == "all year" || notes.contains("wood") || notes.contains("spice")

                // COLD (< 10): Sweet, Vanilla, Leather, Oud
                else -> season == "winter" || notes.contains("vanilla") || notes.contains("oud") || notes.contains("leather")
            }
        }

        val finalPick = suitableScents.randomOrNull() ?: collection.random()
        _recommendation.postValue(finalPick)
    }

    // --- FEATURE 2: DISCOVERY ENGINE (Static Data) ---
    private fun recommendNewScent(temp: Double) {
        val suggestion = when {
            temp >= 25.0 -> "Try 'Dolce & Gabbana Light Blue' for high heat."
            temp >= 20.0 -> "Try 'Bleu de Chanel' for this warm weather."
            temp >= 10.0 -> "Try 'Terre d'Hermes' for cool, crisp air."
            else -> "Try 'Viktor&Rolf Spicebomb' for the cold."
        }
        _discovery.postValue(suggestion)
    }

    // --- FEATURE 3: LONGEVITY TRACKER ---
    fun logSprayTime() {
        val now = System.currentTimeMillis()
        sharedPrefs.edit().putLong("last_spray_time", now).apply()
        updateTimeSinceSpray()
    }

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
}