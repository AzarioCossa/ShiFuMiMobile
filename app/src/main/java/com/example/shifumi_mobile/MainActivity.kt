package com.example.shifumi_mobile

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shifumi_mobile.ui.theme.ShiFuMi_MobileTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppNavigation()
        }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController = navController) }

    composable("playDisplay"){PlayDisplay(navController = navController)}
}}

@Composable
fun HomeScreen(
    
    modifier: Modifier = Modifier,
    navController: NavHostController,
    ){
    Column (modifier = Modifier
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    )   {
        Spacer(modifier = Modifier.height(200.dp))
        Text(
            text ="Shifumi",
            fontSize = 48.sp,
            modifier = Modifier.padding(10.dp)
                .wrapContentWidth(),

        )

        Spacer(modifier = Modifier.height(300.dp))

        Button(onClick = {
            navController.navigate("playDisplay")
        }) {
            Text(
                text = "Jouer",
            )
        }
    }
}


@Composable
fun  PlayDisplay(
    modfier : Modifier = Modifier,
    navController: NavHostController

) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(26.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Text(
            text = "IT'SSSSSSSSS TIMEEEEEEEEEEEEEEEEEEEEEEEEEE",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Red
        )
    }

}
}
