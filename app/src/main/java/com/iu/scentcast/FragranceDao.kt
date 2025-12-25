package com.iu.scentcast

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FragranceDao {

    // 1. Insert a new bottle
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(fragrance: Fragrance)

    // 2. Get all bottles (Flow updates the UI automatically when data changes)
    @Query("SELECT * FROM fragrance_table ORDER BY name ASC")
    fun getAllFragrances(): Flow<List<Fragrance>>

    // 3. Delete a bottle
    @Delete
    suspend fun delete(fragrance: Fragrance)
}