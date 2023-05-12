package com.example.poulpejeu
import android.net.wifi.p2p.WifiP2pInfo
import com.example.poulpejeu.P2P.Server
import kotlin.random.Random

object GameHandler {

    var infoConnection : WifiP2pInfo? = null
    var isOwner: Boolean? = null

    var server: Server? = null

    var practiceMode = false

    var games = IntArray(3)
    var scores = FloatArray(3)
    var otherScore = FloatArray(3)
    var winnerGame = IntArray(3)
    var currentGame = 0

    var winner: Int? = null

    var otherFinished = false

    val QUIZZ = 0
    val SHOUT = 1
    val ROPE = 2
    val BRIDGE = 3
    val BISCUIT = 4
    val SOLEIL = 5


    fun connexionMade(info: WifiP2pInfo){
        infoConnection = info
        isOwner = info.isGroupOwner
    }

    fun clientFinished(score1: Float, score2: Float, score3: Float ){
        scores[0] = score1
        scores[1] = score2
        scores[2] = score3
        otherFinished = true
    }


    fun calculateWinner(){

        for(i in 0..2) {
            winnerOfTheGame(i)
        }

        winner = if(winnerGame.sum() > 0) {
            1
        } else if(winnerGame.sum() < 0){
            0
        }else {
            -1
        }
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

            if (!numbers.contains(randomNumber)) {
                numbers.add(randomNumber)
            }
        }
        games = numbers.toIntArray()
    }
}