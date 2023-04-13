package com.example.poulpejeu

import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import com.example.poulpejeu.R

class RopeGame : ComponentActivity(), GestureDetector.OnGestureListener {

    private lateinit var gestureDetector: GestureDetectorCompat
    private var totalDistance = 0.0
    private var previousY = 0.0
    private var dpi = 0
    private lateinit var distanceText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ropegame_layout)

        dpi = resources.displayMetrics.densityDpi

        distanceText = findViewById<TextView>(R.id.distance)
        distanceText.text = "Distance parcourue : $totalDistance cm"

        gestureDetector = GestureDetectorCompat(this, this)
        gestureDetector.setIsLongpressEnabled(false)
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
            distanceText.text = "Distance parcourue : $totalDistance cm"
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