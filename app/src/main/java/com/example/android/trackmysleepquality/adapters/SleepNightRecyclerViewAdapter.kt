package com.example.android.trackmysleepquality.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.database.SleepNight

class SleepNightRecyclerViewAdapter : ListAdapter<SleepNight,SleepItemViewHolder>(SleepNightDiffCallback()) {

//    var sleepItemsList = listOf<SleepNight>()
//        set(value) {
//            field = value
//            notifyDataSetChanged()
//        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SleepItemViewHolder.from(parent)


    override fun onBindViewHolder(holder: SleepItemViewHolder, position: Int) {
        val resources = holder.itemView.resources
        holder.bind(getItem(position), resources)
    }
}