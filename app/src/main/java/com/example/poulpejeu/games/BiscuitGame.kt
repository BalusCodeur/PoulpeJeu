package com.example.poulpejeu.games

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.os.*
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.view.isVisible
import com.example.poulpejeu.GameHandler
import com.example.poulpejeu.menus.PracticeResult
import com.example.poulpejeu.R
import java.util.concurrent.TimeUnit
import kotlin.math.*
import kotlin.random.Random

class BiscuitGame: ComponentActivity() {
    private lateinit var circleView: CircleView
    private lateinit var starView: StarView
    private lateinit var squareView: SquareView
    private lateinit var count: TextView
    private lateinit var biscuit : ImageView
    private lateinit var biscuitgame : ImageView
    private lateinit var chrono: TextView
    private lateinit var container: FrameLayout

    var end = false
    var play = false
    private var start = 0L
    private lateinit var countDownTimer: CountDownTimer
    private var timeElapsed: Long = 0

    val paint = Paint().apply {
        color = Color.rgb(183, 111, 55)
        style = Paint.Style.STROKE
        strokeWidth = 100f
        alpha = 180
    }

    val transparent = Paint().apply {
        color = Color.TRANSPARENT
        style = Paint.Style.STROKE
        strokeWidth = 5f
    }

    val stylo = Paint().apply {
        color = Color.rgb(160,82,45)
        style = Paint.Style.STROKE
        strokeWidth = 15f
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.biscuit_layout)
        circleView = CircleView(this)
        squareView = SquareView(this)
        starView = StarView(this)

        biscuit = findViewById(R.id.biscuit)
        biscuit.setImageResource(R.drawable.biscuit)

        biscuitgame = findViewById(R.id.biscuitgame)
        biscuitgame.setImageResource(R.drawable.biscuitgame)

        count = findViewById(R.id.count)
        chrono = findViewById(R.id.chrono)
        container = findViewById(R.id.container)
        countDownTimer = object : CountDownTimer(Long.MAX_VALUE, 10) {
            private var startTime: Long = 0

            override fun onTick(millisUntilFinished: Long) {
                if (startTime == 0L) {
                    startTime = SystemClock.elapsedRealtime()
                } else {
                    val elapsedMillis = SystemClock.elapsedRealtime() - startTime
                    timeElapsed += elapsedMillis
                    startTime += elapsedMillis
                    val time = String.format("%02d:%02d.%02d",
                        TimeUnit.MILLISECONDS.toMinutes(timeElapsed),
                        TimeUnit.MILLISECONDS.toSeconds(timeElapsed) % 60,
                        timeElapsed % 100)
                    chrono.text = time
                }
            }

            override fun onFinish() {}
        }

        initGame()
    }

    fun startCountdown(seconds: Int) {
        count.text = seconds.toString()
        if (seconds > 0) {
            Handler().postDelayed({ startCountdown(seconds - 1) }, 1000)
        } else {
            count.isVisible =false
            start = System.currentTimeMillis()
            play = true
            countDownTimer.start()
        }

    }

    fun initGame(){
        val randomNumber = Random.nextInt(3)

        when (randomNumber){
            0 -> circle()
            1 -> square()
            2 -> star()
        }
    }
    fun circle(){
        container.addView(circleView)
        circleView.setBiscuitActivity(this)
        startCountdown(3)
    }

    fun square(){
        container.addView(squareView)
        squareView.setBiscuitActivity(this)
        startCountdown(3)
    }

    fun star(){
        container.addView(starView)
        starView.setBiscuitActivity(this)
        startCountdown(3)
    }

    fun penalty() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

            // Vérifier si la vibration est supportée
            if (vibrator.hasVibrator()) {
                // Faire vibrer l'écran pendant 500 millisecondes
                vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
            }
        }
        timeElapsed +=2000
    }

    fun showScore(){
        end = true
        val scorestr = timeElapsed;
        Log.i("scorestr",scorestr.toString())
        val score = (timeElapsed/1000).toString()+","+ (timeElapsed%1000) + "s"
        if(GameHandler.practiceMode) {
            val intent = Intent(this, PracticeResult::class.java)
            intent.putExtra("score", score)
            intent.putExtra("game","Biscuit")
            val prefs = getSharedPreferences("scores", MODE_PRIVATE)
            val editor = prefs.edit()

            // Enregistrer le score dans les SharedPreferences sous forme de chaîne
            editor.putLong("lastscorelong", scorestr)
            editor.apply()
            startActivity(intent)
        }else {
            GameHandler.scoreText[GameHandler.currentGame] = score
            GameHandler.nextGame(this, timeElapsed.toFloat())
        }
        finish()
    }
}



class CircleView(context: Context) : View(context) {
    private lateinit var biscuitActivity: BiscuitGame
    var centerX = 0f
    var centerY = 0f
    val smallRadius = 425f
    val mediumRadius = 475f
    val bigRadius = 525f
    private var points = mutableListOf<PointF>()
    var distancetrace = 0f
    var trace = false
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        centerX = width / 2f
        centerY = height / 2f

        canvas.drawCircle(centerX, centerY, smallRadius, biscuitActivity.transparent)
        canvas.drawCircle(centerX, centerY, mediumRadius, biscuitActivity.paint)
        canvas.drawCircle(centerX, centerY, bigRadius, biscuitActivity.transparent)

        if (points.size > 1) {
            for (i in 1 until points.size) {
                val lastPoint = points[i - 1]
                val currentPoint = points[i]
                canvas.drawLine(lastPoint.x, lastPoint.y, currentPoint.x, currentPoint.y, biscuitActivity.stylo)
            }
        }
    }
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (biscuitActivity.play) {
            val x = event.x
            val y = event.y
            val distance = sqrt((x - centerX).pow(2) + (y - centerY).pow(2))
            if (isCircle(points) && !biscuitActivity.end) {
                biscuitActivity.showScore()
            }
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (points.size > 1) {
                        distancetrace = sqrt(
                            (x - points[points.size - 1].x).pow(2) + (y - points[points.size - 1].y).pow(
                                2
                            )
                        );
                    }
                    // Le doigt touche l'écran, on initialise les coordonnées de départ
                    if (distancetrace < 70f) {
                        if (distance <= smallRadius) {
                            // L'utilisateur touche l'intérieur du petit cercle
                            biscuitActivity.penalty()
                        } else if (distance <= bigRadius) {
                            // L'utilisateur touche l'intérieur du grand cercle
                            points.add(PointF(event.x, event.y))
                        } else {
                        }
                        trace = true
                    } else {
                        trace = false
                        if (distance <= smallRadius) {
                            // L'utilisateur touche l'intérieur du petit cercle
                            biscuitActivity.penalty()
                        }
                    }
                    return true
                }
                MotionEvent.ACTION_MOVE -> {
                    // Le doigt se déplace, on dessine une ligne entre les coordonnées précédentes et actuelles
                    val x = event.x
                    val y = event.y
                    val distance = sqrt((x - centerX).pow(2) + (y - centerY).pow(2))
                    if (points.size > 0) {
                        distancetrace = sqrt(
                            (x - points[points.size - 1].x).pow(2) + (y - points[points.size - 1].y).pow(
                                2
                            )
                        );
                    }
                    if (trace) {
                        if (distance <= smallRadius) {
                            // L'utilisateur touche l'intérieur du petit cercle
                            biscuitActivity.penalty()
                            trace = false
                        } else if (distance <= bigRadius) {
                            points.add(PointF(x, y))
                            invalidate()
                        } else {
                            trace = false
                        }
                    }
                    return true
                }
                MotionEvent.ACTION_UP -> {
                    // Le doigt est levé, on termine le dessin de la ligne
                    return true
                }
            }
        }
        return super.onTouchEvent(event)
    }
    private fun isCircle(points: List<PointF>): Boolean {
        if (points.size < 150) return false // Il faut au moins 3 points pour dessiner un cercle
        val firstPoint = points[0]
        val lastPoint = points[points.size - 1]
        val radius = smallRadius // Rayon du cercle intérieur pour les comparaisons
        var distance = 0f
        for (i in 0 until points.size - 1) {
            val p1 = points[i]
            val p2 = points[i + 1]
            distance += sqrt((p2.x - p1.x).pow(2) + (p2.y - p1.y).pow(2))
        }
        val circumference = 2 * PI * radius
        return circumference - distance  < 0f && sqrt((lastPoint.x - firstPoint.x).pow(2) + (lastPoint.y - firstPoint.y).pow(2))<30f
    }


    fun setBiscuitActivity(activity: BiscuitGame) {
        biscuitActivity = activity
    }
}
class SquareView(context: Context?) : View(context) {
    private lateinit var biscuitActivity: BiscuitGame

    private val smallSquareSize = 750f
    private val mediumSquareSize = 850f
    private val largeSquareSize = 950f

    // Récupérez les dimensions de la vue dans la fonction onDraw
    private var centerX = 0f
    private var centerY = 0f
    private var smallSquareX = 0f
    private var smallSquareY = 0f
    private var mediumSquareX = 0f
    private var mediumSquareY = 0f
    private var largeSquareX = 0f
    private var largeSquareY = 0f

    private var points = mutableListOf<PointF>()
    var distancetrace = 0f
    var trace = false


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Récupérez les dimensions de la vue
        val viewWidth = width.toFloat()
        val viewHeight = height.toFloat()

        // Définir les positions des carrés
        centerX = viewWidth / 2f
        centerY = viewHeight / 2f

        smallSquareX = centerX - smallSquareSize / 2
        smallSquareY = centerY - smallSquareSize / 2

        mediumSquareX = centerX - mediumSquareSize / 2
        mediumSquareY = centerY - mediumSquareSize / 2

        largeSquareX = centerX - largeSquareSize / 2
        largeSquareY = centerY - largeSquareSize / 2

        // Dessinez les carrés
        canvas.drawRect(largeSquareX, largeSquareY, largeSquareX + largeSquareSize, largeSquareY + largeSquareSize, biscuitActivity.transparent)
        canvas.drawRect(mediumSquareX, mediumSquareY, mediumSquareX + mediumSquareSize, mediumSquareY + mediumSquareSize, biscuitActivity.paint)
        canvas.drawRect(smallSquareX, smallSquareY, smallSquareX + smallSquareSize, smallSquareY + smallSquareSize, biscuitActivity.transparent)

        if (points.size > 1) {
            for (i in 1 until points.size) {
                val lastPoint = points[i - 1]
                val currentPoint = points[i]
                canvas.drawLine(lastPoint.x, lastPoint.y, currentPoint.x, currentPoint.y, biscuitActivity.stylo)
            }
        }
    }
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (biscuitActivity.play) {
            if (isSquare(points)&& !biscuitActivity.end) {
                biscuitActivity.showScore()
            }
            val x = event.x
            val y = event.y
            val smallSquareRect = RectF(
                smallSquareX,
                smallSquareY,
                smallSquareX + smallSquareSize,
                smallSquareY + smallSquareSize
            )
            val largeSquareRect = RectF(
                largeSquareX,
                largeSquareY,
                largeSquareX + largeSquareSize,
                largeSquareY + largeSquareSize
            )
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (points.size > 1) {
                        distancetrace = sqrt(
                            (x - points[points.size - 1].x).pow(2) + (y - points[points.size - 1].y).pow(
                                2
                            )
                        );
                    }
                    // Le doigt touche l'écran, on initialise les coordonnées de départ
                    if (distancetrace < 70f) {
                        if (smallSquareRect.contains(x, y)) {
                            // L'utilisateur touche l'intérieur du petit cercle
                            biscuitActivity.penalty()
                        } else if (!largeSquareRect.contains(x, y)) {
                        } else {
                            // L'utilisateur touche l'intérieur du grand cercle
                            points.add(PointF(event.x, event.y))
                        }
                        trace = true
                    } else {
                        trace = false
                        if (smallSquareRect.contains(x, y)) {
                            // L'utilisateur touche l'intérieur du petit cercle
                            biscuitActivity.penalty()
                        }
                    }
                    return true
                }
                MotionEvent.ACTION_MOVE -> {
                    // Le doigt se déplace, on dessine une ligne entre les coordonnées précédentes et actuelles
                    val x = event.x
                    val y = event.y
                    val distance = sqrt((x - centerX).pow(2) + (y - centerY).pow(2))
                    if (points.size > 0) {
                        distancetrace = sqrt(
                            (x - points[points.size - 1].x).pow(2) + (y - points[points.size - 1].y).pow(
                                2
                            )
                        );
                    }
                    if (trace) {
                        if (smallSquareRect.contains(x, y)) {
                            // L'utilisateur touche l'intérieur du petit cercle
                            biscuitActivity.penalty()
                            trace = false
                        } else if (largeSquareRect.contains(x, y)) {
                            points.add(PointF(x, y))
                            invalidate()
                        } else {
                            trace = false
                        }
                    }
                    return true
                }
                MotionEvent.ACTION_UP -> {
                    // Le doigt est levé, on termine le dessin de la ligne
                    return true
                }
            }
        }
        return super.onTouchEvent(event)
    }
    fun isSquare(points: List<PointF>): Boolean {
        if (points.size < 150) return false // Il faut au moins 3 points pour dessiner un cercle
        val firstPoint = points[0]
        val lastPoint = points[points.size - 1]
        var distance = 0f
        for (i in 0 until points.size - 1) {
            val p1 = points[i]
            val p2 = points[i + 1]
            distance += sqrt((p2.x - p1.x).pow(2) + (p2.y - p1.y).pow(2))
        }
        val circumference = 3000f
        return circumference - distance  < 0f && sqrt((lastPoint.x - firstPoint.x).pow(2) + (lastPoint.y - firstPoint.y).pow(2))<30f
    }

    fun setBiscuitActivity(activity: BiscuitGame) {
        biscuitActivity = activity
    }
}
class StarView(context: Context?) : View(context) {
    private lateinit var biscuitActivity: BiscuitGame

    // Définir la taille des étoiles
    val largeStarSize =  1100f
    val mediumStarSize = 900f
    val smallStarSize = 700f
    var centerX = 0f
    var centerY = 0f
    val angle = 360f / 5f
    val innerAngle = angle / 2f
    lateinit var smallPath : Path
    lateinit var mediumPath : Path
    lateinit var bigPath : Path

    private var points = mutableListOf<PointF>()
    var distancetrace = 0f
    var trace = false

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Définir les coordonnées du centre des étoiles
        centerX = width.toFloat() / 2f
        centerY = height.toFloat() / 2f

        // Définir les angles pour les branches des étoiles
        val angleOffset = -90f

        // Dessiner les étoiles
        smallPath = drawStar(canvas, centerX, centerY, smallStarSize, angle, innerAngle, angleOffset, biscuitActivity.transparent)
        mediumPath = drawStar(canvas, centerX, centerY, mediumStarSize, angle, innerAngle, angleOffset, biscuitActivity.paint)
        bigPath = drawStar(canvas, centerX, centerY, largeStarSize, angle, innerAngle, angleOffset, biscuitActivity.transparent)

        if (points.size > 1) {
            for (i in 1 until points.size) {
                val lastPoint = points[i - 1]
                val currentPoint = points[i]
                canvas.drawLine(lastPoint.x, lastPoint.y, currentPoint.x, currentPoint.y, biscuitActivity.stylo)
            }
        }
    }
    private fun drawStar(canvas: Canvas, centerX: Float, centerY: Float, starSize: Float, angle: Float, innerAngle: Float, angleOffset: Float, paint: Paint):Path {
        // Définir les points pour dessiner l'étoile
        val path = Path()
        for (i in 0 until 5) {
            val x = centerX + starSize / 2f * cos(Math.toRadians((i * angle + angleOffset).toDouble())).toFloat()
            val y = centerY + starSize / 2f * sin(Math.toRadians((i * angle+ angleOffset).toDouble())).toFloat()
            if (i == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
            val innerX = centerX + starSize / 4f * cos(Math.toRadians((i * angle + angleOffset+ innerAngle).toDouble())).toFloat()
            val innerY = centerY + starSize / 4f * sin(Math.toRadians((i * angle+ angleOffset + innerAngle).toDouble())).toFloat()
            path.lineTo(innerX, innerY)
        }
        path.close()

        // Dessiner l'étoile
        canvas.drawPath(path, paint)

        return path
    }
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (biscuitActivity.play) {
            if (isStar(points)&& !biscuitActivity.end) {
                biscuitActivity.showScore()
            }
            val x = event.x
            val y = event.y
            val isInSmallStar = isPointInsideStar(x, y, smallPath)
            val isInBigStar = isPointInsideStar(x, y, bigPath)
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (points.size > 1) {
                        distancetrace = sqrt(
                            (x - points[points.size - 1].x).pow(2) + (y - points[points.size - 1].y).pow(
                                2
                            )
                        );
                    }
                    // Le doigt touche l'écran, on initialise les coordonnées de départ
                    if (distancetrace < 50f) {
                        if (isInSmallStar) {
                            // L'utilisateur touche l'intérieur du petit cercle
                            biscuitActivity.penalty()
                        } else if (!isInBigStar) {
                        } else {
                            // L'utilisateur touche l'intérieur du grand cercle
                            points.add(PointF(event.x, event.y))
                        }
                        trace = true
                    } else {
                        trace = false
                        if (isInSmallStar) {
                            // L'utilisateur touche l'intérieur du petit cercle
                            biscuitActivity.penalty()
                        }
                    }
                    return true
                }
                MotionEvent.ACTION_MOVE -> {
                    // Le doigt se déplace, on dessine une ligne entre les coordonnées précédentes et actuelles
                    if (points.size > 0) {
                        distancetrace = sqrt(
                            (x - points[points.size - 1].x).pow(2) + (y - points[points.size - 1].y).pow(
                                2
                            )
                        );
                    }
                    if (trace) {
                        if (isInSmallStar) {
                            // L'utilisateur touche l'intérieur du petit cercle
                            biscuitActivity.penalty()
                            trace = false
                        } else if (isInBigStar) {
                            points.add(PointF(x, y))
                            invalidate()
                        } else {
                            trace = false
                        }
                    }
                    return true
                }
                MotionEvent.ACTION_UP -> {
                    // Le doigt est levé, on termine le dessin de la ligne
                    return true
                }
            }
        }
        return super.onTouchEvent(event)
    }
    private fun isPointInsideStar(x: Float, y: Float, starPath: Path): Boolean {
        // Vérifier si le point se trouve à l'intérieur de la zone délimitée par l'étoile
        val bounds = RectF()
        starPath.computeBounds(bounds, true)
        if (!bounds.contains(x, y)) {
            return false
        }

        // Vérifier si le point se trouve à l'intérieur de l'étoile en utilisant la géométrie analytique
        val starRegion = Region()
        starRegion.setPath(starPath, Region(bounds.left.toInt(), bounds.top.toInt(), bounds.right.toInt(), bounds.bottom.toInt()))
        return starRegion.contains(x.toInt(), y.toInt())
    }
    private fun isStar(points: List<PointF>): Boolean {
        if (points.size < 150) return false // Il faut au moins 3 points pour dessiner un cercle
        val firstPoint = points[0]
        val lastPoint = points[points.size - 1]
        var distance = 0f
        for (i in 0 until points.size - 1) {
            val p1 = points[i]
            val p2 = points[i + 1]
            distance += sqrt((p2.x - p1.x).pow(2) + (p2.y - p1.y).pow(2))
        }
        val circumference = 2000f
        return circumference - distance  < 0f && sqrt((lastPoint.x - firstPoint.x).pow(2) + (lastPoint.y - firstPoint.y).pow(2))<30f
    }

    fun setBiscuitActivity(activity: BiscuitGame) {
        biscuitActivity = activity
    }
}