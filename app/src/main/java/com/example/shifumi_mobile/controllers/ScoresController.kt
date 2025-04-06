package com.example.shifumi_mobile.controllers

import android.content.Context
import java.io.File
import java.io.FileOutputStream
import java.io.FileInputStream

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
                    "${parts[0]}: ${currentScore + newScore}"
                } else {
                    line
                }
            }.joinToString("\n")
        } else {
            scores + "\n$player: $newScore"
        }

        FileOutputStream(file).use { output ->
            output.write(updatedScores.toByteArray())
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