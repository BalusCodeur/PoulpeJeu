package com.example.poulpejeu

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.ComponentActivity

class Practice : ComponentActivity() {

    private lateinit var title: ImageView
    private lateinit var bandeSon: MediaPlayer
    private lateinit var buttonShout: Button
    private lateinit var buttonQuizz: Button
    private lateinit var buttonSoleil: Button
    private lateinit var buttonBiscuit: Button
    private lateinit var buttonBridge: Button
    private lateinit var buttonRope: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.practice_menu_layout)

        title = findViewById(R.id.poulpejeu)
        bandeSon = MediaPlayer.create(this,R.raw.bandeson)
        buttonShout = findViewById(R.id.shoutButton)
        buttonQuizz = findViewById(R.id.quizzButton)
        buttonSoleil = findViewById(R.id.soleilButton)
        buttonBiscuit = findViewById(R.id.biscuitButton)
        buttonBridge = findViewById(R.id.bridgeButton)
        buttonRope = findViewById(R.id.ropeButton)

        title.setImageResource(R.drawable.poulpejeu)

        bandeSon.setOnCompletionListener { bandeSon.start() }

        bandeSon.start()


        buttonShout.setOnClickListener {
            // Créez une Intent pour ouvrir votre nouvelle page
            val intent = Intent(this, ShoutGame::class.java)
            intent.putExtra("mode",1)
            startActivity(intent)
        }

        buttonQuizz.setOnClickListener {
            // Créez une Intent pour ouvrir votre nouvelle page
            val intent = Intent(this, Quizz::class.java)
            intent.putExtra("mode",0)
            startActivity(intent)
        }

        buttonSoleil.setOnClickListener {
            // Créez une Intent pour ouvrir votre nouvelle page
            val intent = Intent(this, Soleil123::class.java)
            intent.putExtra("mode",0)
            startActivity(intent)
            finish()
        }
        buttonBiscuit.setOnClickListener {
            // Créez une Intent pour ouvrir votre nouvelle page
            val intent = Intent(this, Biscuit::class.java)
            intent.putExtra("mode",0)
            startActivity(intent)
        }
        buttonBridge.setOnClickListener {
            // Créez une Intent pour ouvrir votre nouvelle page
            val intent = Intent(this, BridgeGame::class.java)
            intent.putExtra("mode",0)
            startActivity(intent)
        }
        buttonRope.setOnClickListener {
            // Créez une Intent pour ouvrir votre nouvelle page
            val intent = Intent(this, RopeGame::class.java)
            intent.putExtra("mode",0)
            startActivity(intent)
        }

    }
    override fun onPause() {
        super.onPause()
        bandeSon.pause()
    }

    override fun onResume() {
        super.onResume()
        bandeSon.start()
    }
}