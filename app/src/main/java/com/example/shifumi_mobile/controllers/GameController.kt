package com.example.shifumi_mobile.controllers
import com.example.shifumi_mobile.R

class GameController {
    val computerChoice = listOf(
        R.drawable.rock,
        R.drawable.paper,
        R.drawable.scisors)

    fun playGame(): Int {
        return gameResult()
    }


    fun gameResult(): Int {
        return computerChoice.random()
    }
}