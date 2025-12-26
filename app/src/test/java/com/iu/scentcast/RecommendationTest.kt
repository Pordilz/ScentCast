package com.iu.scentcast

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for the [RecommendationEngine] logic.
 * These tests ensure that the fragrance filtering logic works correctly across different
 * temperature ranges without needing an Android device or database.
 */
class RecommendationTest {

    // Mock Data for testing
    private val summerScent = Fragrance(name = "Aqua", house = "Brand", notes = "Citrus", season = "Summer")
    private val winterScent = Fragrance(name = "Cozy", house = "Brand", notes = "Vanilla", season = "Winter")
    private val allYearScent = Fragrance(name = "Blue", house = "Brand", notes = "Floral", season = "All Year")

    private val myCollection = listOf(summerScent, winterScent, allYearScent)

    /**
     * Verifies that hot weather (e.g., 30°C) correctly filters for summer fragrances.
     */
    @Test
    fun `test Hot Weather returns Summer Scents`() {
        // Given: It is 30 degrees (Hot)
        val temp = 30.0

        // When: We run the engine
        val results = RecommendationEngine.filterByWeather(temp, myCollection)

        // Then: result should contain 'Aqua' (summer) but NOT 'Cozy' (winter)
        assertTrue(results.contains(summerScent))
        assertTrue(!results.contains(winterScent))
    }

    /**
     * Verifies that cold weather (e.g., 5°C) correctly filters for winter fragrances.
     */
    @Test
    fun `test Cold Weather returns Winter Scents`() {
        // Given: It is 5 degrees (Cold)
        val temp = 5.0

        // When: We run the engine
        val results = RecommendationEngine.filterByWeather(temp, myCollection)

        // Then: result should contain 'Cozy' (winter)
        assertTrue(results.contains(winterScent))
    }

    /**
     * Verifies that the engine handles an empty collection gracefully.
     */
    @Test
    fun `test Empty Collection returns Empty List`() {
        val results = RecommendationEngine.filterByWeather(25.0, emptyList())
        assertEquals(0, results.size)
    }
}