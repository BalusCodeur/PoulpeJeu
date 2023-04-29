package com.example.poulpejeu

import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Handler
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.poulpejeu.R
import java.lang.Math.log10
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.view.isVisible
import java.lang.Thread.sleep
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


        val button: Button = findViewById(R.id.button)
        var volume = 0.0;

        val progressBar: ProgressBar = findViewById(R.id.progress_bar)
        progressBar.max = 100
        progressBar.progress = 0
        progressBar.isVisible = false

        var count: TextView = findViewById(R.id.count)
        count.isVisible = false

        var result: TextView = findViewById(R.id.result)
        result.isVisible = false


        fun getAmplitude(): Double {
            return 20 * kotlin.math.log10(mRecorder.maxAmplitude.toDouble())
        }

        fun endGame(){
            progressBar.isVisible = false
            count.isVisible = false
            result.text = "Volume atteint : " + ((volume * 100.0).roundToInt()/100.0).toString() + " dB"
            result.isVisible = true
        }

        fun startGame() {
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
                        endGame()
                        mRecorder.stop()
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


        button.setOnClickListener {
            mRecorder.start()
            button.isVisible = false
            count.isVisible = true
            startCountdown(3)
        }
    }
}