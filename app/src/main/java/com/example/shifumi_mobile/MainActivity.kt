package com.example.shifumi_mobile

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.navigation.compose.*
import com.example.shifumi_mobile.wifi.WifiManager
import com.example.shifumi_mobile.models.ShakeListener
import com.example.shifumi_mobile.ui.GameMode
import com.example.shifumi_mobile.ui.HomeScreen
import com.example.shifumi_mobile.ui.PlayScreen
import com.example.shifumi_mobile.ui.WifiScreen
import com.example.shifumi_mobile.ui.getPlayerName
import com.example.shifumi_mobile.ui.showScores

class MainActivity : ComponentActivity() {
    private lateinit var wifiManager: WifiManager
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private lateinit var shakeListener: ShakeListener
    private val isShaken = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        wifiManager = WifiManager(this)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        shakeListener = ShakeListener {
            isShaken.value = true
        }

        setContent {
            AppNavigation(isShaken, wifiManager, this)
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
        wifiManager.closeConnection()
        sensorManager.unregisterListener(shakeListener)
    }
}

@Composable
fun AppNavigation(isShaken: MutableState<Boolean>, wifiManager: WifiManager, context: Context) {
//fun AppNavigation(isShaken: MutableState<Boolean>, context: Context = LocalContext.current) {
    val navController = rememberNavController()
    var playerName by remember { mutableStateOf("") }

    NavHost(navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("wifi") { WifiScreen(wifiManager) }
        composable("getPlayerName") { getPlayerName(navController) { name -> playerName = name }}
        composable("gameMode") { GameMode(navController, playerName) }
        composable("PlayClassique") { PlayScreen(navController, isShaken, "PlayClassique", playerName, context) }
        composable("PlayStrategie"){ PlayScreen(navController, isShaken, "PlayStrategie", playerName, context) }
        composable("scores") { showScores(navController, context) }
    }
}
