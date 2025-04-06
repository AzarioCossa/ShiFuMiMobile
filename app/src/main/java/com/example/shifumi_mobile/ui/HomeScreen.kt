package com.example.shifumi_mobile.ui

import android.content.Context
import com.example.shifumi_mobile.ui.ImageBackground

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shifumi_mobile.controllers.ScoresController

@Composable
fun HomeScreen(navController: NavHostController) {
    ImageBackground(1f)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(26.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Shifumi!",
            fontSize = 34.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { navController.navigate("getPlayerName") },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        ) {
            Text(text = "Jouer")
        }
        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { navController.navigate("wifi") },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        ) {
            Text(text = "WiFi")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { navController.navigate("scores") },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        ) {
            Text(text = "Scores")
        }


    }
}

@Composable
fun getPlayerName(navController: NavHostController, onNameEntered: (String) -> Unit){
    var playerName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(26.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Entrez votre nom :",
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            value = playerName,
            onValueChange = { playerName = it },
            label = { Text("Nom") }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                // Save the player name and navigate to the game mode screen
                onNameEntered(playerName)
                navController.navigate("gameMode")

            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        ) {
            Text(text = "Valider")
        }
    }
}

@Composable
fun showScores(navController: NavHostController, context: Context) {
    val scoresController = ScoresController()
    val scores = scoresController.readScores(context).toList().sortedByDescending { (_, score) -> score }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(26.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { navController.navigate("home") },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        ) {
            Text(text = "Retour à l'accueil")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Scores",
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Display the scores here
        scores.forEach { (player, score) ->
            Text(text = "$player: $score", fontSize = 20.sp)
            Spacer(modifier = Modifier.height(10.dp))
        }


    }
}