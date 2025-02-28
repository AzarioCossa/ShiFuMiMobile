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
            val images = gameController.playGame() // Récupère les 2 images (joueur + ordinateur)
            playerImageResId = images[0] // Image du joueur
            computerImageResId = images[1] // Image de l'ordinateur

            result = "Résultat: ${gameController.getLastResult()}"
            isShaken.value = false
        }
    }

    ImageBackground(0.5f)

    Button(
        onClick = { navController.navigate("home") },
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        modifier = Modifier
            .padding(10.dp)
            .padding(top = 26.dp),
    ) {
        Text(text = "Retour à l'accueil")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(26.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(bottom = 40.dp)
        ) {
            Text(
                text = "Ordinateur",
                fontSize = 25.sp
            )
            Box(modifier = Modifier.size(200.dp)) {
                Image(
                    painter = painterResource(id = computerImageResId),
                    contentDescription = "Choix de l'ordinateur",
                    modifier = Modifier.size(200.dp)
                )
            }
        }

        Text(
            text = result.ifEmpty { "Secouez le portable pour jouer" },
            fontSize = 18.sp
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 40.dp)
        ) {
            Text(
                text = "Vous",
                fontSize = 25.sp
            )
            Box(modifier = Modifier.size(200.dp)) {
                Image(
                    painter = painterResource(id = playerImageResId),
                    contentDescription = "Votre choix",
                    modifier = Modifier.size(200.dp)
                )
            }
        }
    }
}

