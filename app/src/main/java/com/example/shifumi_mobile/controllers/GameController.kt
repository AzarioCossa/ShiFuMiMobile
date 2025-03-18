package com.example.shifumi_mobile.controllers

import com.example.shifumi_mobile.R
import kotlin.random.Random

class GameController {
    private var lastResult: String = ""

    fun playGame(): Triple<Int, Int, String> {
        val choices = listOf("Pierre", "Papier", "Ciseaux")
        val playerChoice = choices.random()
        val computerChoice = choices.random()

        
        lastResult = "L'ordinateur a choisi: $computerChoice"
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

    fun getLastResult(): String {
        return lastResult
    }

    fun determineWinner(playerChoice: String, computerChoice: String): String {
        return if (playerChoice == computerChoice) {
            "Match nul !"
        } else if (
            (playerChoice == "Pierre" && computerChoice == "Ciseaux") ||
            (playerChoice == "Ciseaux" && computerChoice == "Papier") ||
            (playerChoice == "Papier" && computerChoice == "Pierre")
        ) {
            "Vous avez gagné !"
        } else {
            "L'ordinateur a gagné !"
        }
    }


}

