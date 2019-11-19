package com.example.android.trackmysleepquality.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.database.SleepNight

class SleepNightRecyclerViewAdapter : ListAdapter<SleepNight, SleepItemViewHolder>(SleepNightDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SleepItemViewHolder.from(parent)

    override fun onBindViewHolder(holder: SleepItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}