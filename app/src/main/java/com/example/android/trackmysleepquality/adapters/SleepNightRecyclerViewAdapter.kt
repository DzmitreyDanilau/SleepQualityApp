package com.example.android.trackmysleepquality.adapters

import android.content.res.Resources
import android.graphics.drawable.Drawable
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

class SleepNightRecyclerViewAdapter : RecyclerView.Adapter<SleepNightRecyclerViewAdapter.SleepItemViewHolder>() {


    var sleepItemsList = listOf<SleepNight>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SleepItemViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.sleep_list_item, parent, false))

    override fun getItemCount() = sleepItemsList.size

    override fun onBindViewHolder(holder: SleepItemViewHolder, position: Int) {
        val resources = holder.itemView.resources
        holder.bind(sleepItemsList[position], resources)
    }


    inner class SleepItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
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

    }
}