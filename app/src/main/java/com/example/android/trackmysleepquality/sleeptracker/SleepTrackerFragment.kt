package com.example.android.trackmysleepquality.sleeptracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.SleepDatabase
import com.example.android.trackmysleepquality.databinding.FragmentSleepTrackerBinding
import com.example.android.trackmysleepquality.viewmodels.SleepTrackerViewModel
import com.example.android.trackmysleepquality.viewmodels.SleepTrackerViewModelFactory

class SleepTrackerFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentSleepTrackerBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_sleep_tracker, container, false)
        val application = requireNotNull(activity).application
        val db = SleepDatabase.getInstance(application).sleepDataBaseDao
        val viewModelFactory = SleepTrackerViewModelFactory(db, application)
        val viewModel = ViewModelProviders
                .of(this, viewModelFactory).get(SleepTrackerViewModel::class.java)
        binding.lifecycleOwner = this
        binding.sleepTrackerViewModel = viewModel
        return binding.root
    }
}
