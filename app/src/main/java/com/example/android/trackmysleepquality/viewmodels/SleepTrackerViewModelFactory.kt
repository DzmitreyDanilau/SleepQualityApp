package com.example.android.trackmysleepquality.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.trackmysleepquality.database.SleepDatabaseDao

class SleepTrackerViewModelFactory(
        private val dSource: SleepDatabaseDao,
        private val application: Application) : ViewModelProvider.Factory {

    @Suppress("uncheked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SleepTrackerViewModel::class.java)) {
            return SleepTrackerViewModel(dSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}