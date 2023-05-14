package com.example.poulpejeu.games

import android.content.Intent
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Handler
import androidx.activity.ComponentActivity
import com.example.poulpejeu.R
import android.widget.*
import androidx.core.view.isVisible
import com.example.poulpejeu.GameHandler
import com.example.poulpejeu.menus.PracticeResult
import kotlin.math.roundToInt

class ShoutGame : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shoutgame_layout)

        val title: ImageView = findViewById(R.id.shoutgame)
        title.setImageResource(R.drawable.shoutgame)


        val mRecorder = MediaRecorder()
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        mRecorder.setOutputFile("/dev/null")
        mRecorder.prepare()


        var volume = 0.0;

        val progressBar: ProgressBar = findViewById(R.id.progress_bar)
        progressBar.max = 100
        progressBar.progress = 0
        progressBar.isVisible = false

        val count: TextView = findViewById(R.id.count)
        count.isVisible = false

        val result: TextView = findViewById(R.id.result)
        result.isVisible = false


        fun getAmplitude(): Double {
            return 20 * kotlin.math.log10(mRecorder.maxAmplitude.toDouble())
        }


        fun showScore() {
            val score = "Volume atteint : " + ((volume * 100.0).roundToInt()/100.0).toString() + " dB"
            val bundle = intent.extras
            if(GameHandler.practiceMode) {
                val intent = Intent(this, PracticeResult::class.java)
                intent.putExtra("score", score)
                startActivity(intent)
                finish()
            }else {
                GameHandler.scoreText[GameHandler.currentGame] = score
                GameHandler.nextGame(this, volume.toFloat())
                finish()
            }
        }

        fun startGame() {
            mRecorder.start()
            val handler2 = Handler()
            handler2.post(object : Runnable {
                override fun run() {

                    count.text = "CRIEZ"

                    var temp: Double

                    progressBar.isVisible = true

                    if (progressBar.progress < 100) {
                        progressBar.progress += 1

                        temp = getAmplitude()
                        if (volume < temp) {
                            volume = temp
                        }
                        handler2.postDelayed(this, 30)
                    }
                    else {
                        mRecorder.stop()
                        showScore()

                    }
                }
            })
        }

        fun startCountdown(seconds: Int) {
            count.text = seconds.toString()
            if (seconds > 0) {
                Handler().postDelayed({ startCountdown(seconds - 1) }, 1000)
            } else {
                startGame()
            }
        }

        count.isVisible = true
        startCountdown(3)
    }
}