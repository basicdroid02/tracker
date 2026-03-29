package com.example.tracker.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.tracker.data.local.entity.PeriodEntity
import kotlinx.coroutines.flow.Flow

/**
 * This is your interface for the two methods you requested.
 * I’ve added Flow, which is a reactive stream.
 * This means your UI will automatically refresh as soon as you add a new period.
 */
@Dao
interface PeriodDao {

    // Method 1: Add Period
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPeriod(period: PeriodEntity)

    // Method 2: List Periods (ordered by newest first)
    @Query("SELECT * FROM period_table ORDER BY startDate DESC")
    fun getAllPeriods(): Flow<List<PeriodEntity>>

    // Method 3: Delete Period
    // Room uses the primary key to find and delete
    @Delete
    suspend fun deletePeriod(period: PeriodEntity)

    // Method 4: Update Period
    @Update
    suspend fun update(period: PeriodEntity)
}