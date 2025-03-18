package com.example.shifumi_mobile

import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.navigation.compose.*
import com.example.shifumi_mobile.bluetooth.BluetoothManager
import com.example.shifumi_mobile.models.ShakeListener
import com.example.shifumi_mobile.ui.HomeScreen
import com.example.shifumi_mobile.ui.PlayScreen

class MainActivity : ComponentActivity() {
    private lateinit var bluetoothManager: BluetoothManager
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private lateinit var shakeListener: ShakeListener
    private val isShaken = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        bluetoothManager = BluetoothManager(this)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        if (bluetoothManager.checkPermissions(this)) {
            bluetoothManager.enableBluetooth(this)
        }

        shakeListener = ShakeListener {
            isShaken.value = true
        }

        setContent {
            AppNavigation(isShaken)
        }
    }

    override fun onResume() {
        super.onResume()
        accelerometer?.let {
            sensorManager.registerListener(shakeListener, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(shakeListener)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1001) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                bluetoothManager.enableBluetooth(this)
            } else {
                Toast.makeText(this, "Permissões necessárias para usar o Bluetooth", Toast.LENGTH_LONG).show()
            }
        }
    }
}

@Composable
fun AppNavigation(isShaken: MutableState<Boolean>) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("play") { PlayScreen(navController, isShaken) }
    }
}