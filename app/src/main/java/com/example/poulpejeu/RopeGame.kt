package com.example.poulpejeu

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.view.GestureDetectorCompat
import kotlin.math.roundToInt

class RopeGame : ComponentActivity(), GestureDetector.OnGestureListener {

    private lateinit var gestureDetector: GestureDetectorCompat
    private var totalDistance = 0.0
    private var previousY = 0.0
    private var dpi = 0
    private lateinit var distanceText: TextView
    private lateinit var corde: ImageView
    private lateinit var timeLeft: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ropegame_layout)

        val title: ImageView = findViewById(R.id.ropegame)
        title.setImageResource(R.drawable.ropegame)

        dpi = resources.displayMetrics.densityDpi

        distanceText = findViewById<TextView>(R.id.distance)
        distanceText.text = "Distance parcourue : "+ ((totalDistance * 10.0).roundToInt()/10.0).toString() +" cm"

        gestureDetector = GestureDetectorCompat(this, this)
        gestureDetector.setIsLongpressEnabled(false)
        timeLeft = findViewById(R.id.chrono)

        corde = findViewById<ImageView>(R.id.cordeview)

        object : CountDownTimer(20000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Mise à jour de l'affichage du temps restant
                val secondsLeft = millisUntilFinished / 1000
                // Par exemple, vous pouvez mettre à jour un TextView avec le temps restant :
                timeLeft.text = "$secondsLeft"
            }

            override fun onFinish() {
                // La minuterie est terminée, vous pouvez appeler votre fonction end ici
                //end()
                showScore()

            }
        }.start()


    }


    fun showScore() {
        val score = "Corde déroulée : " + ((totalDistance * 10.0).roundToInt()/10.0).toString() + " cm"
        val bundle = intent.extras
        if(GameHandler.practiceMode) {
            val intent = Intent(this, PracticeResult::class.java)
            intent.putExtra("score", score)
            startActivity(intent)
            finish()
        }else {
            GameHandler.scoreText[GameHandler.currentGame] = score
            GameHandler.nextGame(this, totalDistance.toFloat())
            finish()
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    override fun onDown(event: MotionEvent): Boolean {
        previousY = event.y.toDouble()
        return true
    }

    override fun onScroll(
        event1: MotionEvent,
        event2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        val deltaY = event2.y - previousY

        previousY = event2.y.toDouble()

        if (deltaY > 0) {
            totalDistance += deltaY / (dpi / 2.54)
            distanceText.text = "Distance parcourue : "+ ((totalDistance * 10.0).roundToInt()/10.0).toString() + " cm"
            corde.translationY += deltaY.toFloat();
            if(corde.translationY > 0){
                corde.translationY = -1500f;
            }
        }

        return true
    }

    override fun onFling(
        event1: MotionEvent,
        event2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        return true
    }

    override fun onLongPress(event: MotionEvent) {}

    override fun onShowPress(event: MotionEvent) {}

    override fun onSingleTapUp(event: MotionEvent): Boolean {
        return true
    }
}