package com.example.poulpejeu
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.wifi.p2p.WifiP2pInfo
import androidx.core.content.ContextCompat.startActivity
import com.example.poulpejeu.P2P.Message
import com.example.poulpejeu.P2P.MessageTransferService
import com.example.poulpejeu.P2P.Server
import com.example.poulpejeu.P2P.WifiDirectActivity
import com.example.poulpejeu.Result
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlin.random.Random

object GameHandler {

    var infoConnection : WifiP2pInfo? = null
    var isOwner: Boolean? = null

    var server: Server? = null

    var practiceMode = false

    var games = IntArray(3)
    var scores = FloatArray(3)
    var scoreText = arrayOf("","","")
    var otherScore = FloatArray(3)
    var winnerGame = IntArray(3)
    var currentGame = 0

    var winner: Int? = null

    var otherFinished = false
    var finished = false

    val QUIZZ = 0
    val SHOUT = 1
    val ROPE = 2
    val BRIDGE = 3
    val BISCUIT = 4
    val SOLEIL = 5

    val activityMap = mutableMapOf<Int, Class<out Activity>>()
init {
    activityMap[QUIZZ] = Quizz::class.java
    activityMap[SHOUT] = ShoutGame::class.java
    activityMap[ROPE] = RopeGame::class.java
    activityMap[BRIDGE] = BridgeGame::class.java
    activityMap[BISCUIT] = Biscuit::class.java
    activityMap[SOLEIL] = Soleil123::class.java
}


    fun connexionMade(info: WifiP2pInfo){
        infoConnection = info
        isOwner = info.isGroupOwner
    }

    fun resetData(){
        infoConnection = null
        isOwner = null

        server = null

        practiceMode = false

        games = IntArray(3)
        scores = FloatArray(3)
        scoreText = arrayOf("","","")
        otherScore = FloatArray(3)
        winnerGame = IntArray(3)
        currentGame = 0

        winner = null

        otherFinished = false
        finished = false
    }


    fun calculateWinner(){
        for(i in 0..2) {
            winnerOfTheGame(i)
        }

        winner = winnerGame.sum()

    }

    private fun winnerOfTheGame(gameNumber: Int){
        if(games[gameNumber] == QUIZZ || games[gameNumber] == SHOUT || games[gameNumber] == ROPE || games[gameNumber] == BRIDGE){
            bestScoreHigh(gameNumber)
        }
        else if(games[gameNumber] == BISCUIT || games[gameNumber] == SOLEIL){
            bestScoreLow(gameNumber)

        }
    }

    private fun bestScoreLow(gameNumber: Int){

        winnerGame[gameNumber] = if (scores[gameNumber] < otherScore[gameNumber]) {
                1
             }else if (scores[gameNumber] == otherScore[gameNumber]){
                0
             }else{
                -1
             }
    }

    private fun bestScoreHigh(gameNumber: Int){

        winnerGame[gameNumber] =  if (scores[gameNumber] > otherScore[gameNumber]) {
            1
        }else if (scores[gameNumber] == otherScore[gameNumber]){
            0
        }else{
            -1
        }
    }

    fun pickGames(){
        val random = Random
        val numbers = mutableSetOf<Int>()

        while (numbers.size < 3) {
            val randomNumber = random.nextInt(6)

            if (!numbers.contains(randomNumber) && randomNumber != SOLEIL) {
                numbers.add(randomNumber)
            }
        }
        games = numbers.toIntArray()
    }

    fun nextGame(context: Context, score: Float){
        scores[currentGame] = score
        if(currentGame < 2){
            currentGame++
            val intent = Intent(context, activityMap[games[currentGame]])
            context.startActivity(intent)
        }
        else{
            finished = true
            if(!isOwner!!){
                val msg = Message("scores", scores[0],scores[1],scores[2])

                val serviceIntent = Intent(
                    context,
                    MessageTransferService::class.java
                )
                serviceIntent.action= MessageTransferService.ACTION_SEND_STRING
                serviceIntent.putExtra(MessageTransferService.EXTRAS_STRING, Gson().toJson(msg) )
                serviceIntent.putExtra(
                    MessageTransferService.EXTRAS_GROUP_OWNER_ADDRESS,
                    infoConnection!!.groupOwnerAddress.hostAddress
                )
                serviceIntent.putExtra(MessageTransferService.Companion.EXTRAS_GROUP_OWNER_PORT, 8988)
                context.startService(serviceIntent)
            }
            else{
                Thread.sleep(200)
            }
            val intent = Intent(context, PlayResult::class.java)
            context.startActivity(intent)
        }
    }



}