package com.example.tracker.data.local.entity

import  androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * This defines the "Period object". We use Long for dates because
 * SQLite handles numbers for sorting.
 */
@Entity(tableName ="period_table")
data class PeriodEntity (
    @PrimaryKey(autoGenerate=true)
    val id: Int =0,
    val startDate :Long,
    val endDate: Long
)