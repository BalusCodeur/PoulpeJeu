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

class StartMenu() : ComponentActivity(), Server.ServerCallback, MessageTransferService.ServerResponseCallback {

    private lateinit var title: ImageView
    private lateinit var bandeSon: MediaPlayer
    private lateinit var buttonPlay: Button

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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.start_menu_layout)

        title = findViewById(R.id.poulpejeu)
        bandeSon = MediaPlayer.create(this,R.raw.bandeson)
        buttonPlay = findViewById(R.id.playButton)



        title.setImageResource(R.drawable.poulpejeu)

        bandeSon.setOnCompletionListener { bandeSon.start() }

        bandeSon.start()

        val serviceIntent = Intent(
            this,
            MessageTransferService::class.java
        )

        if(GameHandler.isOwner!!) {
            GameHandler.server!!.setServerCallback(this)
            buttonPlay.isVisible = false
        }else{
            MessageDataHolder.serverResponseCallback = this
        }

        buttonPlay.setOnClickListener {

            serviceIntent.action= MessageTransferService.ACTION_SEND_STRING
            serviceIntent.putExtra(MessageTransferService.EXTRAS_STRING, "start\r\n")
            serviceIntent.putExtra(
                MessageTransferService.EXTRAS_GROUP_OWNER_ADDRESS,
                GameHandler.infoConnection!!.groupOwnerAddress.hostAddress
            )
            serviceIntent.putExtra(MessageTransferService.Companion.EXTRAS_GROUP_OWNER_PORT, 8988)
            this.startService(serviceIntent)



            // Créez une Intent pour ouvrir votre nouvelle page
            //val randomActivities = activityList.shuffled().take(3)
            val randomActivities = listOf(Quizz::class.java,Biscuit::class.java,BridgeGame::class.java)
            val bundle = Bundle()
            bundle.putSerializable("randomActivities", ArrayList(randomActivities))
            bundle.putInt("currentIndex", 0)
            val intent = Intent(this, randomActivities[0])
            intent.putExtra("mode",1)
            Log.i("bundle",bundle.toString())
            intent.putExtras(bundle)
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


    override fun onServerRequestReceived(request: String) {
        runOnUiThread {

        }
    }

    override fun onServerResponseReceived(response: String) {
        runOnUiThread {

        }
    }

}