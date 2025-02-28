package com.example.shifumi_mobile

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.navigation.compose.*
import com.example.shifumi_mobile.bluetooth.BluetoothManager
import com.example.shifumi_mobile.ui.HomeScreen
import com.example.shifumi_mobile.ui.PlayScreen

class MainActivity : ComponentActivity() {
    private lateinit var bluetoothManager: BluetoothManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bluetoothManager = BluetoothManager(this)

        if (bluetoothManager.checkPermissions(this)) {
            bluetoothManager.enableBluetooth(this)
        }

        setContent {
            AppNavigation()
        }
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
fun AppNavigation() {
    val navController = rememberNavController()
    val isShaken = remember { mutableStateOf(false) }

    NavHost(navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("play") { PlayScreen(navController, isShaken) }
    }
}