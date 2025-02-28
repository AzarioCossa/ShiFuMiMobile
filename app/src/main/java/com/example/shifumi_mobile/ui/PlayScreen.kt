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
    var imageResId by remember { mutableStateOf(R.drawable.sun) }
    val gameController = GameController()

    LaunchedEffect(isShaken.value) {
        if (isShaken.value) {
            imageResId = gameController.playGame()
            result = "Résultat: ${gameController.getLastResult()}"
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
            modifier = Modifier.padding(10.dp)
        ) {
            Text(text = "Retour à l'accueil")
        }

        Text(
            text = result.ifEmpty { "Secouez le portable pour jouer" },
            fontSize = 18.sp
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
