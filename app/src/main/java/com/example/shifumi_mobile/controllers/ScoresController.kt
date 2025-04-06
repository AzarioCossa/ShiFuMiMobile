package com.example.shifumi_mobile.controllers

import android.content.Context
import java.io.File
import java.io.FileOutputStream
import java.io.FileInputStream
import android.os.Vibrator
import android.os.VibrationEffect

class ScoresController {
    fun saveScores(context: Context, player: String, newScore: Int) {
        val fileName = "scores.txt"
        val file = File(context.filesDir, fileName)
        val scores = if (file.exists()) {
            FileInputStream(file).use { input ->
                input.readBytes().toString(Charsets.UTF_8)
            }
        } else {
            ""
        }

        val updatedScores = if (scores.contains("$player:")) {
            scores.lines().map { line ->
                val parts = line.split(": ")
                if (parts[0] == player) {
                    val currentScore = parts[1].toIntOrNull() ?: 0
                    val newTotalScore = currentScore + newScore
                    if (newTotalScore % 5 == 0) {
                        vibrate(context)
                    }
                    "${parts[0]}: $newTotalScore"
                } else {
                    line
                }
            }.joinToString("\n")
        } else {
            if (newScore % 5 == 0) {
                vibrate(context)
            }
            scores + "\n$player: $newScore"
        }

        FileOutputStream(file).use { output ->
            output.write(updatedScores.toByteArray())
        }
    }

    private fun vibrate(context: Context) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) {
            val vibrationEffect = VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(vibrationEffect)
        }
    }

    fun readScores(context: Context): Map<String, Int> {
        val fileName = "scores.txt"
        val file = File(context.filesDir, fileName)
        return if (file.exists()) {
            FileInputStream(file).use { input ->
                input.readBytes().toString(Charsets.UTF_8)
            }.lines().mapNotNull { line ->
                val parts = line.split(": ")
                if (parts.size == 2) {
                    val score = parts[1].toIntOrNull()
                    if (score != null) {
                        parts[0] to score
                    } else {
                        null
                    }
                } else {
                    null
                }
            }.toMap()
        } else {
            emptyMap()
        }
    }
}