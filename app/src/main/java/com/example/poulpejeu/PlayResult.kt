package com.example.poulpejeu

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.view.isVisible
import com.example.poulpejeu.P2P.MessageDataHolder
import com.example.poulpejeu.P2P.MessageTransferService
import com.example.poulpejeu.P2P.Server

class PlayResult : ComponentActivity(), Server.ServerCallback, MessageTransferService.ServerResponseCallback {
    private lateinit var returnMenuButton:Button
    private lateinit var score1:TextView
    private lateinit var score2:TextView
    private lateinit var score3:TextView
    private lateinit var waiting:TextView
    private lateinit var winner:ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.play_result_layout)
        returnMenuButton = findViewById(R.id.returnMenu)
        score1 = findViewById(R.id.score1)
        score2 = findViewById(R.id.score2)
        score3 = findViewById(R.id.score3)
        waiting = findViewById(R.id.waiting)
        winner = findViewById(R.id.winner)

        if(GameHandler.isOwner!!) {
            GameHandler.server!!.setServerCallback(this)
        }else{
            MessageDataHolder.serverResponseCallback = this
        }


            if(GameHandler.otherFinished){
                score1.text = "jeu 1 : ${GameHandler.scoreText[0]}"
                score2.text = "jeu 2 : ${GameHandler.scoreText[1]}"
                score3.text = "jeu 3 : ${GameHandler.scoreText[2]}"
                waiting.isVisible = false
                annonceWinner()
            }
            else {
                score1.isVisible = false
                score2.isVisible = false
                score3.isVisible = false
                winner.isVisible = false
                returnMenuButton.isVisible = false
            }


        returnMenuButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun annonceWinner(){
        if(GameHandler.isOwner!!){
            if(GameHandler.winner!! > 0) winner.setImageResource(R.drawable.gagnant)
            else if(GameHandler.winner!! < 0) winner.setImageResource(R.drawable.perdant)
            else winner.setImageResource(R.drawable.egalite)
        }
        else{
            if(GameHandler.winner!! > 0) winner.setImageResource(R.drawable.perdant)
            else if(GameHandler.winner!! < 0) winner.setImageResource(R.drawable.gagnant)
            else winner.setImageResource(R.drawable.egalite)
        }

        winner.isVisible = true

    }



    override fun onServerRequestReceived(request: String) {
        runOnUiThread {
            if(request == "finish"){
                score1.text = "jeu 1 : ${GameHandler.scoreText[0]}"
                score2.text = "jeu 2 : ${GameHandler.scoreText[1]}"
                score3.text = "jeu 3 : ${GameHandler.scoreText[2]}"
                score1.isVisible = true
                score2.isVisible = true
                score3.isVisible = true
                waiting.isVisible = false
                returnMenuButton.isVisible = true
                annonceWinner()
            }

        }
    }

    override fun onServerResponseReceived(response: String) {
        runOnUiThread {
            if(response == "finish"){
                score1.text = "jeu 1 : ${GameHandler.scoreText[0]}"
                score2.text = "jeu 2 : ${GameHandler.scoreText[1]}"
                score3.text = "jeu 3 : ${GameHandler.scoreText[2]}"
                score1.isVisible = true
                score2.isVisible = true
                score3.isVisible = true
                waiting.isVisible = false
                returnMenuButton.isVisible = true
                annonceWinner()
            }

        }
    }
}