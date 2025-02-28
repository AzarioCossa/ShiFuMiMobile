package com.example.shifumi_mobile.controllers

import com.example.shifumi_mobile.R
import kotlin.random.Random

class GameController {
    private var lastResult: String = ""

    fun playGame(): Int {
        val choices = listOf("Pierre", "Papier", "Ciseaux")
        val choice = choices.random()

        lastResult = "L'ordinateur a choisi: $choice"

        return when (choice) {
            "Pierre" -> R.drawable.rock
            "Papier" -> R.drawable.paper
            "Ciseaux" -> R.drawable.scisors
            else -> R.drawable.sun
        }
    }

    fun getLastResult(): String {
        return lastResult
    }
}
