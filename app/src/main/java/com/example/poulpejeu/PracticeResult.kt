package com.example.poulpejeu

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity

class PracticeResult : ComponentActivity() {
    private lateinit var score:TextView
    private lateinit var back:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.practice_result_layout)
        score = findViewById(R.id.score)
        back = findViewById(R.id.backButton)
        // Récupérer le score depuis l'intent
        val scoreActivity = intent.getStringExtra("score")
        // Afficher le score dans le TextView
        score.text = scoreActivity

        back.setOnClickListener {
            //val intent = Intent(this, Practice::class.java)
            //startActivity(intent)
            finish()
        }
    }
}