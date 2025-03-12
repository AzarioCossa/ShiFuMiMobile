package com.example.shifumi_mobile.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.shifumi_mobile.controllers.GameController
import com.example.shifumi_mobile.R

@Composable
fun PlayScreen(navController: NavHostController, isShaken: MutableState<Boolean>) {
    var result by remember { mutableStateOf("") }
    var playerImageResId by remember { mutableStateOf(R.drawable.sun) }
    var computerImageResId by remember { mutableStateOf(R.drawable.sun) }
    val gameController = GameController()

    LaunchedEffect(isShaken.value) {
        if (isShaken.value) {
            val (playerImage, computerImage, winnerMessage) = gameController.playGame()
            playerImageResId = playerImage
            computerImageResId = computerImage
            result = winnerMessage
            isShaken.value = false
        }
    }

    ImageBackground(0.5f)

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
            modifier = Modifier.padding(bottom = 20.dp)
        ) {
            Text(text = "Retour à l'accueil")
        }

        //Choix ordinateur
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(bottom = 40.dp)
        ) {
            Text(text = "Ordinateur", fontSize = 25.sp)
            Image(
                painter = painterResource(id = computerImageResId),
                contentDescription = "Choix de l'ordinateur",
                modifier = Modifier.size(200.dp)
            )
        }

        //Resultat
        Text(
            text = result.ifEmpty { "Secouez le portable pour jouer" },
            fontSize = 22.sp,
            modifier = Modifier.padding(vertical = 20.dp)
        )

        //Choix du joueur
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Vous", fontSize = 25.sp)
            Image(
                painter = painterResource(id = playerImageResId),
                contentDescription = "Votre choix",
                modifier = Modifier.size(200.dp)
            )
        }
    }
}
