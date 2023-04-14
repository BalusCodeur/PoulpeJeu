package com.example.poulpejeu

import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.poulpejeu.R
import java.lang.Math.log10
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import java.lang.Thread.sleep

class ShoutGame : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shoutgame_layout)


        val mRecorder = MediaRecorder()
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        mRecorder.setOutputFile("/dev/null")
        mRecorder.prepare()
        mRecorder.start()


        val button: Button = findViewById(R.id.button)
        var volume: Double = 0.0;

        val progressBar: ProgressBar = findViewById(R.id.progress_bar)
        progressBar.max = 100
        progressBar.progress = 0

        fun getAmplitude(): Double {
            return 20 * kotlin.math.log10(mRecorder.maxAmplitude.toDouble())
        }

        button.setOnClickListener {

            val handler = Handler()
            handler.post(object : Runnable {
                override fun run() {
                    var temp: Double
                    // Mettre à jour la barre de progression

                    if (progressBar.progress < 100) {
                        progressBar.progress += 1

                        temp = getAmplitude()
                        if (volume < temp) {
                            volume = temp
                        }
                        handler.postDelayed(this, 30)
                    }
                    else {
                        Toast.makeText(this@ShoutGame, volume.toString() + " dB", Toast.LENGTH_LONG).show()
                        mRecorder.stop()
                    }
                }
            })
        }
    }
}