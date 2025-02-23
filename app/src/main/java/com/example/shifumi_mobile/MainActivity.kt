package com.example.shifumi_mobile

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shifumi_mobile.controllers.GameController
import com.example.shifumi_mobile.models.ShakeListener
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButtonDefaults.containerColor

class MainActivity : ComponentActivity() {
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private lateinit var shakeListener: ShakeListener
    private val isShaken = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

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
    ImageBackground(alphaValue = 1f)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(26.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(200.dp))
        Text(
            text = "Shifumi",
            fontSize = 48.sp,
            modifier = Modifier.padding(10.dp),
            color = Color.White
        )
        Spacer(modifier = Modifier.height(300.dp))
        Button(onClick = { navController.navigate("playDisplay") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF3C102))) {
            Text(text = "Jouer")
        }
    }
}

@Composable
fun PlayDisplay(navController: NavHostController, isShaken: MutableState<Boolean>) {
    var result by remember { mutableStateOf("") }
    var imageResId by remember { mutableStateOf(R.drawable.sun) }
    val gameController = GameController()
    var newImageResId : Int
    var shakeTimes by remember { mutableStateOf(0) }

    LaunchedEffect(isShaken.value) {
        if (isShaken.value) {
            if (shakeTimes == 3) {
                newImageResId = gameController.playGame()
                imageResId = newImageResId
                shakeTimes=0
            }else{
                shakeTimes++
            }

            gameController.addShake()
            result = "Sécouez le portable pour jouer"
            //reset value
            isShaken.value = false
        }
    }

    ImageBackground(alphaValue = 0.5f)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(26.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { navController.navigate("home") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF3C102)),
            modifier = Modifier.padding(10.dp)
        ) {
            Text(text = "Revenir à l'accueil")
        }

        Text(
            text = if (result.isNotEmpty()) result else "Secouez le portable pour commencer",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Box(modifier = Modifier.size(200.dp)) {
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = null,
                modifier = Modifier.size(200.dp)
            )
        }
    }
}


@Composable
fun ImageBackground(alphaValue: Float) {
    Image(
        painter = painterResource(id = R.drawable.bg_image),
        contentDescription = null,
        modifier = Modifier.fillMaxSize()
            .alpha(alphaValue),
        contentScale = ContentScale.Crop
    )
}