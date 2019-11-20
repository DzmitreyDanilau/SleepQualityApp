//package com.example.android.trackmysleepquality.permissions
//
//import android.app.AlertDialog
//import android.content.DialogInterface
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.provider.Settings
//import android.util.Log
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import androidx.core.content.ContextCompat.startActivity
//import com.example.android.trackmysleepquality.sleeptracker.PERMISSION_REQUEST_CODE
//
//if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//    hasPermissions()
//}
//
//fun hasPermissions(): Boolean {
//    val permissionsNeeded = mutableListOf<String>()
//    for (permission in appPermissions) {
//        if (ContextCompat.checkSelfPermission(activity!!.applicationContext, permission) != PackageManager.PERMISSION_GRANTED) {
//            permissionsNeeded.add(permission)
//        }
//    }
//    if (permissionsNeeded.isNotEmpty()) {
//        val tmp = Array(permissionsNeeded.size) { "" }
//        var counter = 0
//        permissionsNeeded.forEach {
//            tmp[counter] = it
//            counter++
//        }
//        Log.d("SleepTrackerFragment", "Permissions need to be granted: $tmp")
//        ActivityCompat.requestPermissions(activity!!, tmp, PERMISSION_REQUEST_CODE)
//        return false
//    }
//    return true
//}
//
//override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//    if (requestCode == PERMISSION_REQUEST_CODE) {
//        val requestPermissionsResult = mutableMapOf<String, Int>()
//        var deniedCount = 0
//        for (i in grantResults.indices) {
//            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
//                requestPermissionsResult[permissions[i]] = grantResults[i]
//                deniedCount++
//            }
//        }
//        if (deniedCount == 0) {
//            Log.d("SleepTrackerFragment", "All permissions allow by user")
//        } else {
//            for (entry in requestPermissionsResult.entries) {
//                val permissionName = entry.key
//                val permissionResult = entry.value
//                if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!, permissionName)) {
//                    showAlertDialog("", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
//                            "Go to the Settings",
//                            DialogInterface.OnClickListener { dialog, which ->
//                                dialog.dismiss()
//                                val intent = Intent(Settings.ACTION_SETTINGS)
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                                startActivity(intent)
//                            },
//                            "Nope, Exit app",
//                            DialogInterface.OnClickListener { dialog, which ->
//                                dialog.dismiss()
//                                activity!!.finish()
//                            }, false)
//                    break
//                }
//            }
//        }
//    }
//}
//
//fun showAlertDialog(title: String,
//                    msg: String,
//                    positiveLable: String,
//                    positiveOnclick: DialogInterface.OnClickListener,
//                    negativeLable: String,
//                    negativeOnClick: DialogInterface.OnClickListener,
//                    isCancable: Boolean): AlertDialog {
//    val builder = AlertDialog.Builder(activity)
//    builder.setTitle(title)
//    builder.setCancelable(isCancable)
//    builder.setMessage(msg)
//    builder.setPositiveButton(positiveLable, positiveOnclick)
//    builder.setNegativeButton(negativeLable, negativeOnClick)
//    val alert = builder.create()
//    alert.show()
//    return alert
//}