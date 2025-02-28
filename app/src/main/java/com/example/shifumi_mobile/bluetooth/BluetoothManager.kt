package com.example.shifumi_mobile.bluetooth

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class BluetoothManager(private val context: Context) {

    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        val bluetoothManager = context.getSystemService(BluetoothManager::class.java)
        bluetoothManager?.adapter
    }

    fun checkPermissions(activity: Activity): Boolean {
        val permissions = arrayOf(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        val missingPermissions = permissions.filter {
            ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
        }

        if (missingPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(activity, missingPermissions.toTypedArray(), 1001)
            return false
        }

        return true
    }

    fun enableBluetooth(activity: Activity) {
        if (checkPermissions(activity)) {
            try {
                if (bluetoothAdapter?.isEnabled == false) {
                    bluetoothAdapter?.enable() // Pode não funcionar em Android 10+
                }
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        }
    }

    fun getPairedDevices(activity: Activity): Set<BluetoothDevice>? {
        return try {
            if (checkPermissions(activity)) {
                bluetoothAdapter?.bondedDevices
            } else {
                null
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
            null
        }
    }
}