package com.example.android.trackmysleepquality.sleeptracker

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.adapters.SleepNightRecyclerViewAdapter
import com.example.android.trackmysleepquality.database.SleepDatabase
import com.example.android.trackmysleepquality.databinding.FragmentSleepTrackerBinding
import com.example.android.trackmysleepquality.viewmodels.SleepTrackerViewModel
import com.example.android.trackmysleepquality.viewmodels.SleepTrackerViewModelFactory
import com.google.android.material.snackbar.Snackbar

const val PERMISSION_REQUEST_CODE = 9

class SleepTrackerFragment : Fragment() {
    lateinit var viewModel: SleepTrackerViewModel
    lateinit var adapter: SleepNightRecyclerViewAdapter
    lateinit var binding: FragmentSleepTrackerBinding
    private val appPermissions = listOf(Manifest.permission.READ_CONTACTS, Manifest.permission.READ_SMS,
            Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hasPermissions()
        }

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
                adapter.submitList(it)
            }
        })
    }

    private fun initRecyclerView() {
        adapter = SleepNightRecyclerViewAdapter()
        binding.rvSleepList.adapter = adapter
    }


    fun hasPermissions(): Boolean {
        val permissionsNeeded = mutableListOf<String>()
        for (permission in appPermissions) {
            if (ContextCompat.checkSelfPermission(activity!!.applicationContext, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded.add(permission)
            }
        }
        if (permissionsNeeded.isNotEmpty()) {
            val tmp = Array(permissionsNeeded.size) { "" }
            var counter = 0
            permissionsNeeded.forEach {
                tmp[counter] = it
                counter++
            }
            Log.d("SleepTrackerFragment", "Permissions need to be granted: $tmp")
            ActivityCompat.requestPermissions(activity!!, tmp, PERMISSION_REQUEST_CODE)
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            val requestPermissionsResult = mutableMapOf<String, Int>()
            var deniedCount = 0
            for (i in grantResults.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    requestPermissionsResult[permissions[i]] = grantResults[i]
                    deniedCount++
                }
            }
            if (deniedCount == 0) {
                Log.d("SleepTrackerFragment", "All permissions allow by user")
            } else {
                for (entry in requestPermissionsResult.entries) {
                    val permissionName = entry.key
                    val permissionResult = entry.value
                    if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!, permissionName)) {
                        showAlertDialog("", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                                "Go to the Settings",
                                DialogInterface.OnClickListener { dialog, which ->
                                    dialog.dismiss()
                                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    startActivity(intent)
                                }

                                )
                    }
                }
            }
        }
    }

    fun showAlertDialog(title: String,
                        msg: String,
                        positiveLable: String,
                        positiveOnclick: DialogInterface.OnClickListener,
                        negativeLable: String,
                        negativeOnClick: DialogInterface.OnClickListener,
                        isCancable: Boolean): AlertDialog {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(title)
        builder.setCancelable(isCancable)
        builder.setMessage(msg)
        builder.setPositiveButton(positiveLable, positiveOnclick)
        builder.setNegativeButton(negativeLable, negativeOnClick)
        val alert = builder.create()
        alert.show()
        return alert
    }
}

