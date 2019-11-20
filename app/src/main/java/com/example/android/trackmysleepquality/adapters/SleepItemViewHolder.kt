package com.example.android.trackmysleepquality.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.databinding.SleepListItemBinding
import com.example.android.trackmysleepquality.databinding.SleepListItemGridBinding
import com.example.android.trackmysleepquality.sleeptracker.OnSleepNightListener

class SleepItemViewHolder private constructor(private val binding: SleepListItemGridBinding
)
    : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: SleepNight, listener: OnSleepNightListener) {
        binding.sleep = item
        binding.clickListener = listener
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): SleepItemViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = SleepListItemGridBinding.inflate(layoutInflater, parent, false)
            return SleepItemViewHolder(binding)
        }
    }
}