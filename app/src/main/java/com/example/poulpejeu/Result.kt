package com.example.poulpejeu

import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity

class Result : ComponentActivity() {
    private lateinit var score:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.result_layout)
        score = findViewById(R.id.score)
        // Récupérer le score depuis l'intent
        val scoreActivity = intent.getStringExtra("test")
        // Afficher le score dans le TextView
        score.text = scoreActivity
    }
}