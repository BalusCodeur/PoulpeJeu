package com.example.poulpejeu

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.poulpejeu.P2P.MessageDataHolder
import com.example.poulpejeu.P2P.MessageTransferService
import com.example.poulpejeu.P2P.Server
import com.example.poulpejeu.P2P.WifiDirectActivity

class MainActivity : ComponentActivity() {

    private lateinit var title: ImageView
    private lateinit var bandeSon: MediaPlayer
    private lateinit var buttonPractice: Button
    private lateinit var buttonP2P: Button

    private val activityList = listOf(
        ShoutGame::class.java,
        Soleil123::class.java,
        BridgeGame::class.java,
        Biscuit::class.java,
        RopeGame::class.java,
        Quizz::class.java
    )
    private var currentIndex = 0


    private val RECORD_AUDIO_PERMISSION_REQUEST_CODE = 1

    // Demander l'autorisation à l'utilisateur
    private fun requestRecordAudioPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.RECORD_AUDIO),
                RECORD_AUDIO_PERMISSION_REQUEST_CODE
            )
        }
    }

    // Gérer la réponse de l'utilisateur à la demande d'autorisation
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_menu_layout)

        title = findViewById(R.id.poulpejeu)
        bandeSon = MediaPlayer.create(this, R.raw.bandeson)
        buttonPractice = findViewById(R.id.practiceButton)
        buttonP2P = findViewById(R.id.P2PButton)



        title.setImageResource(R.drawable.poulpejeu)

        bandeSon.setOnCompletionListener { bandeSon.start() }

        bandeSon.start()


        buttonP2P.setOnClickListener {
            // Créez une Intent pour ouvrir votre nouvelle page
            val intent = Intent(this, WifiDirectActivity::class.java)
            startActivity(intent)
        }

        buttonPractice.setOnClickListener {
            // Créez une Intent pour ouvrir votre nouvelle page
            val intent = Intent(this, Practice::class.java)
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