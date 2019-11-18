package com.example.android.trackmysleepquality.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.formatNights
import kotlinx.coroutines.*

class SleepTrackerViewModel(private val db: SleepDatabaseDao, application: Application)
    : AndroidViewModel(application) {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val nights = db.getAllNights()
    val nightsString = Transformations.map(nights) {
        formatNights(it, application.resources)
    }
    private var tonight = MutableLiveData<SleepNight?>()

    init {
        initTonight()
    }

    private fun initTonight() {
        uiScope.launch {
            tonight.value = getTonightFromDB()
        }
    }

    private suspend fun getTonightFromDB(): SleepNight? {
        return withContext(Dispatchers.IO) {
            var night = db.getTonight()
            if (night?.endTimeMillis != night?.startTimeMilli) {
                night = null
            }
            night
        }
    }

    //bind to View
    fun onStartTracking() {
        uiScope.launch {
            val newNight = SleepNight()
            insert(newNight)
            tonight.value = getTonightFromDB()
        }
    }

    private suspend fun insert(night: SleepNight) {
        withContext(Dispatchers.IO) {
            db.insert(night)
        }
    }

    //Bind to View
    fun onStopTracking() {
        uiScope.launch {
            val oldNight = tonight.value ?: return@launch
            oldNight.endTimeMillis = System.currentTimeMillis()
            update(oldNight)
        }
    }

    private suspend fun update(night: SleepNight) {
        withContext(Dispatchers.IO) {
            db.update(night)
        }
    }

    fun onClear() {
        uiScope.launch {
            clear()
            tonight.value = null
        }
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            db.clear()
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}

