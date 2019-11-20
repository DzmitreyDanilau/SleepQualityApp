package com.example.android.trackmysleepquality.sleepquality

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.SleepDatabase
import com.example.android.trackmysleepquality.databinding.FragmentSleepQualityBinding
import com.example.android.trackmysleepquality.viewmodels.SleepQualityViewModel
import com.example.android.trackmysleepquality.viewmodels.SleepQualityViewModelFactory

class SleepQualityFragment : Fragment() {
    lateinit var viewModel: SleepQualityViewModel
    lateinit var fragmentArgs: SleepQualityFragmentArgs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            fragmentArgs = SleepQualityFragmentArgs.fromBundle(it)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding: FragmentSleepQualityBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_sleep_quality, container, false)
        val application = requireNotNull(this.activity).application
        val db = SleepDatabase.getInstance(application).sleepDataBaseDao
        val viewModelFactory = SleepQualityViewModelFactory(fragmentArgs.sleepNightKey, db)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SleepQualityViewModel::class.java)
        binding.sleepQualityViewModel = viewModel
        subscribeObservers()
        return binding.root
    }

    private fun subscribeObservers() {
        viewModel.navigateToSleeptracker.observe(this, Observer {
            if (it == true) {
                findNavController().navigate(SleepQualityFragmentDirections.actionSleepQualityFragmentToSleepTrackerFragment())
                viewModel.doNavigating()
            }
        })
    }
}
