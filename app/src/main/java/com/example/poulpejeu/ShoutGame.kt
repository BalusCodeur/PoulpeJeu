package com.example.poulpejeu

import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.poulpejeu.R
import java.lang.Math.log10
import android.util.Log

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

        fun getAmplitude(): Double {
            return if (mRecorder != null) {
                20 * log10(mRecorder.maxAmplitude.toDouble());
            } else {
                0.0
            }
        }

        button.setOnClickListener {
            Thread {
                var temp: Double;
                // Créez une Intent pour ouvrir votre nouvelle page
                for (i in 1..100) {
                    Thread.sleep(30)
                    temp = getAmplitude();
                    if (volume < temp){
                        volume = temp;
                    }
                }
                Log.i("volume :", volume.toString())
            }.start()
        }
    }
}