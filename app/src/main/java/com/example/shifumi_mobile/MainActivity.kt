package com.example.shifumi_mobile

import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shifumi_mobile.controllers.GameController
import com.example.shifumi_mobile.models.ShakeListener

class MainActivity : ComponentActivity() {
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private lateinit var shakeListener: ShakeListener
    private val isShaken = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inicializa sensor e listener corretamente
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        shakeListener = ShakeListener {
            isShaken.value = tru
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
}

@Composable
fun AppNavigation(isShaken: MutableState<Boolean>) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("playDisplay") { PlayDisplay(navController, isShaken) }
    }
}

@Composable
fun HomeScreen(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(200.dp))
        Text(
            text = "Shifumi",
            fontSize = 48.sp,
            modifier = Modifier.padding(10.dp)
        )
        Spacer(modifier = Modifier.height(300.dp))
        Button(onClick = { navController.navigate("playDisplay") }) {
            Text(text = "Jouer")
        }
    }
}

@Composable
fun PlayDisplay(navController: NavHostController, isShaken: MutableState<Boolean>) {
    var result by remember { mutableStateOf("") }

    LaunchedEffect(isShaken.value) {
        if (isShaken.value) {
            val gameController = GameController()
            result = gameController.playGame()

            // Value reset
            isShaken.value = false
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(26.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (result.isNotEmpty()) result else "Sécouez le portable pour commencer",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Red
        )
    }
}
