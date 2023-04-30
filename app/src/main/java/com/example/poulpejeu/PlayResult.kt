package com.example.poulpejeu

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity

class PlayResult : ComponentActivity() {
    private lateinit var score:TextView
    private lateinit var next:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.play_result_layout)
        score = findViewById(R.id.score)
        next = findViewById(R.id.nextButton)
        intent.getStringExtra("score")?.let { Log.i("intent", it) }
        // Récupérer le score depuis l'intent
        val scoreActivity = intent.getStringExtra("score")
        // Afficher le score dans le TextView
        score.text = scoreActivity

        next.setOnClickListener {
            val bundle = intent.extras
            if (bundle != null) {
                val randomActivities = bundle.getSerializable("randomActivities") as ArrayList<Class<out ComponentActivity>>
                val currentIndex = bundle.getInt("currentIndex") + 1
                if (currentIndex < randomActivities.size) {
                    val intent = Intent(this, randomActivities[currentIndex])
                    intent.putExtras(bundle)
                    intent.putExtra("currentIndex", currentIndex)
                    startActivity(intent)
                } else {
                    // Toutes les activités ont été jouées, faire quelque chose ici
                    val intent = Intent(this,MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            finish()
        }
    }
}