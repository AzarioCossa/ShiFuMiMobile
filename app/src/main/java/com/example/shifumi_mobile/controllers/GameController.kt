package com.example.shifumi_mobile.controllers

import com.example.shifumi_mobile.R
import kotlin.random.Random
object GameController {
    private var lastPlayerChoice: String? = null

    fun playGame(isStrategic: Boolean): Triple<Int, Int, String> {
        val choices = listOf("Pierre", "Papier", "Ciseaux")
        val playerChoice = choices.random() // Toujours aléatoire pour le joueur

        val computerChoice = if (isStrategic) {
            strategicMove(playerChoice) // Mode stratégique
        } else {
            choices.random() // Mode classique (choix aléatoire)
        }

        val winnerMessage = determineWinner(playerChoice, computerChoice)

        fun getImage(choice: String): Int {
            return when (choice) {
                "Pierre" -> R.drawable.rock
                "Papier" -> R.drawable.paper
                "Ciseaux" -> R.drawable.scisors
                else -> R.drawable.sun
            }
        }

        return Triple(getImage(playerChoice), getImage(computerChoice), winnerMessage)
    }

    private fun determineWinner(playerChoice: String, computerChoice: String): String {
        return when {
            playerChoice == computerChoice -> "Match nul !"
            (playerChoice == "Pierre" && computerChoice == "Ciseaux") ||
                    (playerChoice == "Ciseaux" && computerChoice == "Papier") ||
                    (playerChoice == "Papier" && computerChoice == "Pierre") -> "Vous avez gagné !"
            else -> "L'ordinateur a gagné !"
        }
    }

    private fun strategicMove(playerChoice: String): String {
        lastPlayerChoice?.let { last ->
            val counterMove = when (last) {
                "Pierre" -> "Papier"    // Papier bat Pierre
                "Papier" -> "Ciseaux"   // Ciseaux bat Papier
                "Ciseaux" -> "Pierre"   // Pierre bat Ciseaux
                else -> "Pierre"
            }
            lastPlayerChoice = playerChoice
            return counterMove
        }

        lastPlayerChoice = playerChoice
        return listOf("Pierre", "Papier", "Ciseaux").random()
    }
}
