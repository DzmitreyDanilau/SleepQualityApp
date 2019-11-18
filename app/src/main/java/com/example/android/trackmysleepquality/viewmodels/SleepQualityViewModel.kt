package com.example.android.trackmysleepquality.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import kotlinx.coroutines.*

class SleepQualityViewModel(
        private val sleepNightKey: Long = 0L,
        private val db: SleepDatabaseDao) : ViewModel() {
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val _navigateToSleeptracker = MutableLiveData<Boolean?>()
    val navigateToSleeptracker: LiveData<Boolean?>
        get() = _navigateToSleeptracker


    fun onSleepQuality(quality: Int) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val tonight = db.get(sleepNightKey) ?: return@withContext
                tonight.sleepQuality = quality
                db.update(tonight)
            }
            _navigateToSleeptracker.value = true
        }
    }

    fun doNavigating() {
        _navigateToSleeptracker.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}