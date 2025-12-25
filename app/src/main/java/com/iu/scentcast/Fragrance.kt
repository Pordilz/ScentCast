package com.iu.scentcast

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fragrance_table")
data class Fragrance(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val house: String,
    val notes: String,   // e.g., "Citrus, Marine"
    val season: String   // e.g., "Summer"
)