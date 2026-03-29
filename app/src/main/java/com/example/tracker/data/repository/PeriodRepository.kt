package com.example.tracker.data.repository

import com.example.tracker.data.local.dao.PeriodDao
import com.example.tracker.data.local.entity.PeriodEntity
import kotlinx.coroutines.flow.Flow

/**
 * In Clean Architecture, your UI shouldn’t talk directly to the database.
 * You use a Repository to manage the data flow.
 */
class PeriodRepository(private val periodDao: PeriodDao) {

    // Room runs this on a background thread automatically because of suspend
    suspend fun insert(period: PeriodEntity) {
        periodDao.addPeriod(period)
    }

    // Delete period
    suspend fun delete(period: PeriodEntity) {
        periodDao.deletePeriod(period)
    }

    // This returns a stream of data
    val allPeriods: Flow<List<PeriodEntity>> = periodDao.getAllPeriods()

    suspend fun update(period: PeriodEntity) {
        periodDao.update(period)
    }
}