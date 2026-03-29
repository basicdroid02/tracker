package com.example.tracker

import android.app.Application
import com.example.tracker.data.PeriodDatabase
import com.example.tracker.data.repository.PeriodRepository

class MainApplication : Application() {
    val database by lazy { PeriodDatabase.getDatabase( context = this)}
    val repository by lazy { PeriodRepository (database.periodDao()) }

    override fun onCreate() {
        super.onCreate()
    }
}