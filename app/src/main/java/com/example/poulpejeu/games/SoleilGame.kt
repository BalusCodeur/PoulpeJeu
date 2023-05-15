package com.example.poulpejeu.games

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.view.isVisible
import com.example.poulpejeu.GameHandler
import com.example.poulpejeu.menus.PracticeResult
import com.example.poulpejeu.R

class SoleilGame : ComponentActivity() {
    private lateinit var fille: ImageView
    private lateinit var runner: ImageView
    private lateinit var tdm: ImageView
    private lateinit var feu1:ImageView
    private lateinit var feu2:ImageView
    private lateinit var score : TextView
    private lateinit var count : TextView
    val translationAnim = TranslateAnimation(0f, 0f, 0f, -40f) // déplace de 200 pixels vers le haut
    private val counterInterval = 500L // intervalle de comptage en ms
    private val countingPattern = arrayOf(10L, 10L, 10L, 10L) // temps de comptage et de pause en secondes
    private var currentCountIndex = -1 // index actuel dans le tableau de comptage
    private var isCounting = false // indique si le comptage est en cours
    private var counter = 0L
    private var start:Long = 0
    var startgame = false
    var end = false
    var dead = false

    private lateinit var sonTourne : MediaPlayer
    private lateinit var sonCompte : MediaPlayer


    @SuppressLint("MissingInflatedId", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.soleil123_layout)

        count = findViewById(R.id.count)
        count.isVisible = false

        sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
        fille = findViewById(R.id.fille)
        fille.setImageResource(R.drawable.fille)

        runner = findViewById(R.id.runner)
        runner.setImageResource(R.drawable.runner)

        feu1 = findViewById(R.id.light1)
        feu2 = findViewById(R.id.light2)

        feu1.setImageResource(R.drawable.redlight)
        feu2.setImageResource(R.drawable.redlight)




        sonCompte = MediaPlayer.create(this, R.raw.soleil123)
        sonTourne = MediaPlayer.create(this, R.raw.tourne)
        sonCompte.setOnCompletionListener { sonTourne.start() }

        translationAnim.duration = 10 // durée de l'animation en ms

        count.isVisible = true
        startCountdown(3)

        translationAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                if (isCounting) {
                    dead = true
                    end = true
                    showScore()
                }
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
    }
    val sensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            // Traitement des événements d'accéléromètre ici
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            val acceleration = kotlin.math.sqrt(x * x + y * y + z * z)
            //Log.i("Vitesse",acceleration.toString())
            if (acceleration > 15 && runner.translationY >= -1420 && startgame) { // Choisissez un seuil approprié ici
                runner.startAnimation(translationAnim)
                Log.i("Position",runner.translationY.toString())
                //val anim = AnimationUtils.loadAnimation(this, R.anim.running_anim)
                //imageViewRunner.startAnimation(anim)
            }else if(runner.translationY <= -1420 && !end){
                end = true
                showScore()
            }

        }
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
            // Non utilisé ici
        }
    }

    private fun startCounting() {
        currentCountIndex = 0
        startNextCounting()
    }

    private fun startNextCounting() {
            if (currentCountIndex >= countingPattern.size) {
                // Si on a atteint la fin du tableau, le joueur a perdu
                dead=true
                end = true
                showScore()
                return
            }

            val countDuration = countingPattern[currentCountIndex] * 1000L
            val pauseDuration = 3000L

            // Démarre le CountDownTimer pour le comptage
        if(!end){
            feu1.setImageResource(R.drawable.greenlight)
            feu2.setImageResource(R.drawable.greenlight)
            object : CountDownTimer(countDuration, counterInterval) {

                override fun onTick(millisUntilFinished: Long) {
                    counter = millisUntilFinished / 1000L
                    if (counter == 5L) {
                        sonCompte.start()
                    }
                    Log.i("Temps", counter.toString())
                }

                override fun onFinish() {
                    feu1.setImageResource(R.drawable.redlight)
                    feu2.setImageResource(R.drawable.redlight)
                    if(!end) {
                        isCounting = !isCounting
                        // Démarre le CountDownTimer pour la pause
                        object : CountDownTimer(pauseDuration, counterInterval) {
                            override fun onTick(millisUntilFinished: Long) {
                                counter = millisUntilFinished / 1000L
                            }

                            override fun onFinish() {

                                currentCountIndex++

                                isCounting = !isCounting
                                sonTourne.start()
                                startNextCounting()

                            }
                        }.start()
                    }
                }
            }.start()}
        }

    fun showScore() {
        var score = ((System.currentTimeMillis() - start) / 1000.0)
        if(dead){
            score = 1000.0;
        }
        val bundle = intent.extras
        if(GameHandler.practiceMode) {
            val intent = Intent(this, PracticeResult::class.java)
            intent.putExtra("game","Soleil")
            val prefs = getSharedPreferences("scores", MODE_PRIVATE)
            val editor = prefs.edit()
            // Enregistrer le score dans les SharedPreferences sous forme de chaîne
            editor.putLong("lastscorelong", score.toLong())
            editor.apply()
            intent.putExtra("score", "$score secondes")
            startActivity(intent)
            finish()
        }else {
            GameHandler.scoreText[GameHandler.currentGame] = "Ligne atteinte en $score secondes"
            GameHandler.nextGame(this, score.toFloat())
        }
        end = true
    }
    fun startCountdown(seconds: Int) {
        count.text = seconds.toString()
        if (seconds > 0) {
            Handler().postDelayed({ startCountdown(seconds - 1) }, 1000)
        } else {
            count.isVisible=false
            startgame=true
            start = System.currentTimeMillis()
            startCounting()
        }
    }
}