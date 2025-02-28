package com.example.shifumi_mobile.controllers

import com.example.shifumi_mobile.R
import kotlin.random.Random

class GameController {
    private var lastResult: String = ""

    fun playGame(): List<Int> {
        val choices = listOf("Pierre", "Papier", "Ciseaux")
        val playerChoice = choices.random()
        val computerChoice = choices.random()

        
        lastResult = "L'ordinateur a choisi: $computerChoice"


        fun getImage(choice: String): Int {
            return when (choice) {
                "Pierre" -> R.drawable.rock
                "Papier" -> R.drawable.paper
                "Ciseaux" -> R.drawable.scisors
                else -> R.drawable.sun
            }
        }

        return listOf(getImage(playerChoice), getImage(computerChoice))
    }

    fun getLastResult(): String {
        return lastResult
    }
}

