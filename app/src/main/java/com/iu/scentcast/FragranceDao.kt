package com.iu.scentcast

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for the fragrance_table.
 * Defines the database operations for managing fragrances.
 */
@Dao
interface FragranceDao {
    /**
     * Inserts a new fragrance into the database.
     * Ignores the operation if a fragrance with the same ID already exists.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(fragrance: Fragrance)

    /**
     * Retrieves all fragrances from the database, ordered by name alphabetically.
     * Returns a [Flow] to observe changes in the data.
     */
    @Query("SELECT * FROM fragrance_table ORDER BY name ASC")
    fun getAllFragrances(): Flow<List<Fragrance>>

    /**
     * Deletes a fragrance from the database.
     */
    @Delete
    suspend fun delete(fragrance: Fragrance)

    /**
     * Updates an existing fragrance in the database.
     */
    @Update
    suspend fun update(fragrance: Fragrance)
}
