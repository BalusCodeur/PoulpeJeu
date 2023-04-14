package com.example.poulpejeu

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.poulpejeu.ui.theme.PoulpeJeuTheme

class MainActivity : ComponentActivity() {

    private val RECORD_AUDIO_PERMISSION_REQUEST_CODE = 1
    // Demander l'autorisation à l'utilisateur
    private fun requestRecordAudioPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO),
                RECORD_AUDIO_PERMISSION_REQUEST_CODE)
        }
    }

    // Gérer la réponse de l'utilisateur à la demande d'autorisation
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            RECORD_AUDIO_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // L'utilisateur a autorisé l'accès au microphone
                } else {
                    // L'utilisateur a refusé l'autorisation
                }
                return
            }
        }
    }

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
        setContentView(R.layout.mainactivity_layout)

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
            startActivity(intent)
        }

        buttonQuizz.setOnClickListener {
            // Créez une Intent pour ouvrir votre nouvelle page
            val intent = Intent(this, Quizz::class.java)
            startActivity(intent)
        }

        buttonSoleil.setOnClickListener {
            // Créez une Intent pour ouvrir votre nouvelle page
            val intent = Intent(this, Soleil123::class.java)
            startActivity(intent)
        }
        buttonBiscuit.setOnClickListener {
            // Créez une Intent pour ouvrir votre nouvelle page
            val intent = Intent(this, Biscuit::class.java)
            startActivity(intent)
        }
        buttonBridge.setOnClickListener {
            // Créez une Intent pour ouvrir votre nouvelle page
            val intent = Intent(this, BridgeGame::class.java)
            startActivity(intent)
        }
        buttonRope.setOnClickListener {
            // Créez une Intent pour ouvrir votre nouvelle page
            val intent = Intent(this, RopeGame::class.java)
            startActivity(intent)
        }
        requestRecordAudioPermission()
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