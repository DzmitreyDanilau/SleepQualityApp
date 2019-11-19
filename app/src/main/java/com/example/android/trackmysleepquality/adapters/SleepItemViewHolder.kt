package com.example.android.trackmysleepquality.adapters

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.convertDurationToFormatted
import com.example.android.trackmysleepquality.convertNumericQualityToString
import com.example.android.trackmysleepquality.database.SleepNight
import kotlinx.android.synthetic.main.sleep_list_item.view.*

class SleepItemViewHolder private constructor(view: View) : RecyclerView.ViewHolder(view) {
    var qualityImage: ImageView = itemView.quality_image
    var quality: TextView = itemView.quality_string
    var sleepLength: TextView = itemView.sleep_length

    fun bind(item: SleepNight, resources: Resources) {
        qualityImage.setImageResource(selectQualityPicture(item.sleepQuality))
        quality.text = convertDurationToFormatted(item.startTimeMilli, item.endTimeMillis, resources)
        sleepLength.text = convertNumericQualityToString(item.sleepQuality, resources)
    }

    private fun selectQualityPicture(picId: Int) =
            when (picId) {
                0 -> R.drawable.ic_sleep_0
                1 -> R.drawable.ic_sleep_1
                2 -> R.drawable.ic_sleep_2
                3 -> R.drawable.ic_sleep_3
                4 -> R.drawable.ic_sleep_4
                5 -> R.drawable.ic_sleep_5
                else -> R.drawable.ic_sleep_active
            }

    companion object {
        fun from(parent: ViewGroup): SleepItemViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater
                    .inflate(R.layout.sleep_list_item, parent, false)
            return SleepItemViewHolder(view)
        }
    }
}