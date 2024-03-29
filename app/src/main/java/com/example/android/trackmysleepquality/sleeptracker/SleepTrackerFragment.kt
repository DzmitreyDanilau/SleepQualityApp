package com.example.android.trackmysleepquality.sleeptracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.adapters.SleepNightRecyclerViewAdapter
import com.example.android.trackmysleepquality.database.SleepDatabase
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.databinding.FragmentSleepTrackerBinding
import com.example.android.trackmysleepquality.viewmodels.SleepTrackerViewModel
import com.example.android.trackmysleepquality.viewmodels.SleepTrackerViewModelFactory
import com.google.android.material.snackbar.Snackbar


class SleepTrackerFragment : Fragment(), OnSleepNightListener {
    lateinit var viewModel: SleepTrackerViewModel
    lateinit var adapter: SleepNightRecyclerViewAdapter
    lateinit var binding: FragmentSleepTrackerBinding
    lateinit var layoutManager: RecyclerView.LayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_sleep_tracker, container, false)
        val application = requireNotNull(activity).application
        val db = SleepDatabase.getInstance(application).sleepDataBaseDao
        val viewModelFactory = SleepTrackerViewModelFactory(db, application)

        viewModel = ViewModelProviders
                .of(this, viewModelFactory).get(SleepTrackerViewModel::class.java)
        binding.lifecycleOwner = this
        binding.sleepTrackerViewModel = viewModel
        initRecyclerView()
        subscribeObservers()
        return binding.root
    }

    private fun subscribeObservers() {
        viewModel.navigateToSleepQuality.observe(this, Observer {
            it?.let {
                findNavController().navigate(SleepTrackerFragmentDirections.actionSleepTrackerFragmentToSleepQualityFragment(it.nightId))
                viewModel.doneNavigation()
            }
        })
        viewModel.showSnackBarEvent.observe(this, Observer {
            if (it == true) {
                Snackbar.make(
                        activity!!.findViewById(android.R.id.content),
                        getString(R.string.cleared_message),
                        Snackbar.LENGTH_SHORT
                ).show()
                viewModel.doneShowingSnackbar()
            }
        })

        viewModel.nights.observe(this, Observer {
            it?.let {
                adapter.addHeaderAndSubmitList(it)
            }
        })
        viewModel.navigateToSleepDetail.observe(this, Observer {
            it?.let {
                findNavController().navigate(SleepTrackerFragmentDirections
                        .actionSleepTrackerFragmentToSleepDetailFragment(it))
                viewModel.onSleepDetailNavigated()
            }
        })
    }

    private fun initRecyclerView() {
        val manager = GridLayoutManager(activity, 3)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (position) {
                    0 -> 3
                    else -> 1
                }
            }
        }
        binding.rvSleepList.layoutManager = manager
        adapter = SleepNightRecyclerViewAdapter(this)
        binding.rvSleepList.adapter = adapter
    }

    override fun onNightClick(night: SleepNight) {
        Toast.makeText(context, "${night.nightId}", Toast.LENGTH_SHORT).show()
        viewModel.onSleepNightNightClicked(night.nightId)
    }


}

