package com.iu.scentcast

/**
 * Pure logic engine responsible for matching fragrances to weather conditions.
 * Decoupled from Android components to facilitate easy unit testing.
 */
object RecommendationEngine {

    /**
     * Filters a list of fragrances based on the provided temperature.
     * 
     * Logic criteria:
     * - HOT (> 25°C): Prefers "Summer" season or notes like Citrus, Water, Fresh.
     * - WARM (20-24°C): Prefers "Summer", "All Year" seasons or notes like Floral, Fruit.
     * - COOL (10-19°C): Prefers "Winter", "All Year" seasons or notes like Wood, Spice.
     * - COLD (< 10°C): Prefers "Winter" season or notes like Vanilla, Oud, Leather.
     * 
     * @param temp The temperature in Celsius.
     * @param collection The list of available fragrances to filter.
     * @return A list of fragrances that match the weather criteria.
     */
    fun filterByWeather(temp: Double, collection: List<Fragrance>): List<Fragrance> {
        if (collection.isEmpty()) return emptyList()

        return collection.filter { f ->
            val notes = f.notes.lowercase()
            val season = f.season.lowercase()

            when {
                temp >= 25.0 -> season == "summer" || notes.contains("citrus") || notes.contains("water") || notes.contains("fresh")
                temp >= 20.0 -> season == "summer" || season == "all year" || notes.contains("floral") || notes.contains("fruit")
                temp >= 10.0 -> season == "winter" || season == "all year" || notes.contains("wood") || notes.contains("spice")
                else -> season == "winter" || notes.contains("vanilla") || notes.contains("oud") || notes.contains("leather")
            }
        }
    }
}