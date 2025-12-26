package com.iu.scentcast

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Room database class for the application.
 * Defines the database configuration and serves as the main access point to the persisted data.
 */
@Database(entities = [Fragrance::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Provides access to the DAO for fragrance operations.
     */
    abstract fun fragranceDao(): FragranceDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Gets the singleton instance of the AppDatabase.
         * 
         * @param context The application context.
         * @return The database instance.
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fragrance_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}