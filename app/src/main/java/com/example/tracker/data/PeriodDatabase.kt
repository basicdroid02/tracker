package com.example.tracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tracker.data.local.dao.PeriodDao
import com.example.tracker.data.local.entity.PeriodEntity

/**
 * This is the main access point for your local storage.
 */
@Database(
    entities = [PeriodEntity::class],
    version = 1,
    exportSchema = false
)
abstract class PeriodDatabase : RoomDatabase() {

    abstract fun periodDao(): PeriodDao

    companion object {

        @Volatile
        private var INSTANCE: PeriodDatabase? = null

        fun getDatabase(context: Context): PeriodDatabase {
            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PeriodDatabase::class.java,
                    "period_database"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}