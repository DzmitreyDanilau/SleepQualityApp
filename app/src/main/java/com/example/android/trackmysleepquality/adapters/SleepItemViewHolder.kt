package com.example.android.trackmysleepquality.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.databinding.SleepListItemBinding

class SleepItemViewHolder private constructor(private val binding: SleepListItemBinding
)
    : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: SleepNight) {
        binding.sleep = item
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): SleepItemViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = SleepListItemBinding.inflate(layoutInflater, parent, false)
            return SleepItemViewHolder(binding)
        }
    }
}