package com.example.android.trackmysleepquality.database

sealed class DataItem {
    abstract val id: Long

    data class SleepNightItem(val sleepNight: SleepNight) : DataItem(){
        override val id: Long
            get() = sleepNight.nightId
    }
    object Header : DataItem() {
        override val id: Long
            get() = Long.MIN_VALUE
    }

}

