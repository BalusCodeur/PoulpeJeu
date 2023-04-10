package com.example.poulpejeu

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View
import kotlin.math.min

class CircleView(context: Context) : View(context) {
    val circleBounds = RectF()
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = 3 * width / 8f


        val paint = Paint()
        paint.color = Color.RED // Couleur rouge définie dans les ressources
        paint.style = Paint.Style.STROKE // Cercle non rempli
        paint.strokeWidth = 10f

        canvas.drawCircle(centerX, centerY, radius, paint)
    }
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val diameter = min(w, h) * 0.8f // 80% de la plus petite dimension
        val left = (w - diameter) / 2f
        val top = (h - diameter) / 2f
        val right = left + diameter
        val bottom = top + diameter
        circleBounds.set(left, top, right, bottom)
    }


}
