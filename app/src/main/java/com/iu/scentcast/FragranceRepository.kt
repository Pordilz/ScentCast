package com.iu.scentcast

import kotlinx.coroutines.flow.Flow

class FragranceRepository(private val fragranceDao: FragranceDao) {

    // 1. Database Operations (Pass-through to DAO)
    val allFragrances: Flow<List<Fragrance>> = fragranceDao.getAllFragrances()

    suspend fun insert(fragrance: Fragrance) {
        fragranceDao.insert(fragrance)
    }

    // 2. Network Operations (Call Retrofit)
    suspend fun getWeather(): WeatherResponse? {
        return try {
            val response = RetrofitInstance.api.getCurrentWeather(
                Constants.MY_CITY,
                Constants.API_KEY
            )
            if (response.isSuccessful) {
                response.body()
            } else {
                null // Handle error gracefully
            }
        } catch (e: Exception) {
            null // Handle no internet/crash
        }
    }
}