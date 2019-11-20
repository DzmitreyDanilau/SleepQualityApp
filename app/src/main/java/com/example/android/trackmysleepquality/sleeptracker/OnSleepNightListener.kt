package com.example.android.trackmysleepquality.sleeptracker

import com.example.android.trackmysleepquality.database.SleepNight

interface OnSleepNightListener {
    fun onNightClick(night: SleepNight)
}