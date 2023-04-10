package com.example.poulpejeu

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import kotlin.math.pow
import kotlin.math.sqrt

class Biscuit: ComponentActivity() {
    private lateinit var circleView :CircleView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.biscuit_layout)
        val container = findViewById<FrameLayout>(R.id.container)
        circleView = CircleView(this)
        container.addView(circleView)

    }
    override fun onTouchEvent(event: MotionEvent): Boolean {
        // Vérifier si l'utilisateur a touché l'écran
        if (event.action == MotionEvent.ACTION_DOWN) {
            // Vérifier si les coordonnées du toucher se trouvent dans la marge autour du cercle
            val touchX = event.x
            val touchY = event.y
            val margin = 20f // Marge de 20 pixels autour du cercle
            if (touchX >= circleView.circleBounds.left - margin && touchX <= circleView.circleBounds.right + margin
                && touchY >= circleView.circleBounds.top - margin && touchY <= circleView.circleBounds.bottom + margin) {
                // Vérifier si les coordonnées du toucher se trouvent à l'extérieur du cercle
                val distanceFromCenter = sqrt(
                    ((touchX - circleView.circleBounds.centerX()).pow(2) + (touchY - circleView.circleBounds.centerY()).pow(2)).toDouble()
                ).toFloat()
                val radiusWithMargin = circleView.circleBounds.width() / 2f + margin
                if (distanceFromCenter >= radiusWithMargin) {
                    // L'utilisateur a touché le cercle sur les bords
                    // Ajoutez ici votre code pour gérer l'événement de toucher sur les bords du cercle
                    Log.i("Touché", "Bien")
                    return true // Indiquer que l'événement a été traité
                }else Log.i("NUL","NUL")
            }
        }
        return super.onTouchEvent(event)
    }

}