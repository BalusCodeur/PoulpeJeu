package com.example.poulpejeu

import android.content.Intent
import android.media.MediaRecorder
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import com.example.poulpejeu.R
import java.lang.Math.log10

class ShoutGame : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shoutgame_layout)

        val mRecorder = MediaRecorder()
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        mRecorder.prepare()

        val button: Button = findViewById(R.id.button)
        var volume: Double;

        fun getAmplitude(): Double {
            return if (mRecorder != null) {
                20 * log10(mRecorder.maxAmplitude.toDouble())
            } else {
                0.0
            }
        }

        button.setOnClickListener {
            // Créez une Intent pour ouvrir votre nouvelle page
            mRecorder.start()
            Thread.sleep(1_000)
            volume = getAmplitude();
            mRecorder.stop()
            print(volume)
        }
    }
}