package com.example.poulpejeu

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.ImageView
import androidx.activity.ComponentActivity

class Soleil123 : ComponentActivity() {
    private lateinit var fille: ImageView
    private lateinit var runner: ImageView
    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor
    private lateinit var run : Button
    val translationAnim = TranslateAnimation(0f, 0f, 0f, -40f) // déplace de 200 pixels vers le haut
    private val counterInterval = 1000L // intervalle de comptage en ms
    private val countingPattern = arrayOf(10L, 10L, 10L, 10L) // temps de comptage et de pause en secondes
    private var currentCountIndex = -1 // index actuel dans le tableau de comptage
    private var isCounting = false // indique si le comptage est en cours
    private var counter = 0L


    override fun onCreate(savedInstanceState: Bundle?) {
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.soleil123_layout)

        sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
        fille = findViewById(R.id.fille)
        fille.setImageResource(R.drawable.fille)

        runner = findViewById(R.id.runner)
        runner.setImageResource(R.drawable.runner)

        run = findViewById(R.id.Run)

        val animation = AnimationUtils.loadAnimation(this, R.anim.running_anim)
        translationAnim.duration = 10 // durée de l'animation en ms


        translationAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                // Rien à faire ici
            }

            override fun onAnimationEnd(animation: Animation?) {
                // Définir la position finale de l'image une fois l'animation terminée
                runner.translationY -= 20f
            }

            override fun onAnimationRepeat(animation: Animation?) {
                // Rien à faire ici

            }
        })

        run.setOnClickListener {
            runner.startAnimation(translationAnim)
        }
    }
    val sensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            // Traitement des événements d'accéléromètre ici
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            val acceleration = kotlin.math.sqrt(x * x + y * y + z * z)
            Log.i("Vitesse",acceleration.toString())
            if (acceleration > 15 && runner.translationY >= -1260) { // Choisissez un seuil approprié ici
                runner.startAnimation(translationAnim)
                Log.i("Position",runner.translationY.toString())
                //val anim = AnimationUtils.loadAnimation(this, R.anim.running_anim)
                //imageViewRunner.startAnimation(anim)
            }
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
            // Non utilisé ici
        }
    }

    private fun startCountDown() {
        object : CountDownTimer(countingPattern[currentCountIndex] * 1000L, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                // rien à faire ici
            }

            override fun onFinish() {
                currentCountIndex++

                if (currentCountIndex < countingPattern.size) { // Si ce n'est pas le dernier temps de comptage
                    startCountDown()
                } else { // Si c'est le dernier temps de comptage

                }
            }
        }
    }

}