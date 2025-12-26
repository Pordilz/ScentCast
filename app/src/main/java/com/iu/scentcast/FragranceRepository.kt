package com.iu.scentcast

import kotlinx.coroutines.flow.Flow

/**
 * Repository class that abstracts access to multiple data sources (Database and Network).
 * It provides a clean API for data access to the rest of the application.
 */
class FragranceRepository(private val fragranceDao: FragranceDao) {

    /**
     * Observable list of all fragrances from the database.
     */
    val allFragrances: Flow<List<Fragrance>> = fragranceDao.getAllFragrances()

    /**
     * Inserts a fragrance into the database.
     */
    suspend fun insert(fragrance: Fragrance) {
        fragranceDao.insert(fragrance)
    }

    /**
     * Fetches the current weather data from the remote API.
     * 
     * @return [WeatherResponse] if successful, null otherwise.
     */
    suspend fun getWeather(): WeatherResponse? {
        return try {
            val response = RetrofitInstance.api.getCurrentWeather(
                Constants.MY_CITY,
                Constants.API_KEY
            )
            if (response.isSuccessful) {
                response.body()
            } else {
                null // Handle error gracefully (e.g., API error)
            }
        } catch (e: Exception) {
            null // Handle network/parsing exceptions
        }
    }

    /**
     * Deletes a fragrance from the local database.
     */
    suspend fun delete(fragrance: Fragrance) {
        fragranceDao.delete(fragrance)
    }

    /**
     * Updates a fragrance in the local database.
     */
    suspend fun update(fragrance: Fragrance) {
        fragranceDao.update(fragrance)
    }
}