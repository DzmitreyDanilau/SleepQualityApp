package com.example.android.trackmysleepquality.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
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
    val nights = db.getAllNights()
    private val _navigateToSleepQuality = MutableLiveData<SleepNight>()
    val navigateToSleepQuality: LiveData<SleepNight>
        get() = _navigateToSleepQuality
    val nightsString = Transformations.map(nights) {
        formatNights(it, application.resources)
    }
    private var tonight = MutableLiveData<SleepNight?>()
    private var _showSnackbarEvent = MutableLiveData<Boolean>()
    val showSnackBarEvent: LiveData<Boolean>
        get() = _showSnackbarEvent
    private val _navigateToSleepDetail = MutableLiveData<Long>()
    val navigateToSleepDetail: LiveData<Long>
        get() = _navigateToSleepDetail
    val startButtonVisible = Transformations.map(tonight) {
        it == null
    }
    val stopButtonVisible = Transformations.map(tonight) {
        it != null
    }
    val clearButtonVisible = Transformations.map(nights) {
        it?.isNotEmpty()
    }

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
            _navigateToSleepQuality.value = oldNight
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
            _showSnackbarEvent.value = true
        }
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            db.clear()
        }
    }

    fun doneNavigation() {
        _navigateToSleepQuality.value = null
    }

    fun doneShowingSnackbar(){
        _showSnackbarEvent.value = false
    }
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun onSleepNightNightClicked(id: Long) {
        _navigateToSleepDetail.value = id
    }

    fun onSleepDetailNavigated() {
        _navigateToSleepDetail.value = null
    }
}

