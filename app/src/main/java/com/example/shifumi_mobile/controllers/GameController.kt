package com.example.shifumi_mobile.controllers

class GameController {

    fun playGame(): String {
        val computerChoice = listOf("rock", "paper", "scissors").random()
        return computerChoice
    }
}