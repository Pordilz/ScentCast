package com.iu.scentcast

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class representing a Fragrance entity in the Room database.
 * 
 * @property id Unique identifier for the fragrance (auto-generated).
 * @property name The name of the fragrance (e.g., "Aventus").
 * @property house The brand or perfume house (e.g., "Creed").
 * @property notes The scent profile/notes (e.g., "Citrus, Wood"). Used for filtering by weather.
 * @property season The ideal season for wearing the fragrance (e.g., "Summer").
 */
@Entity(tableName = "fragrance_table")
data class Fragrance(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val house: String,
    val notes: String,
    val season: String
)