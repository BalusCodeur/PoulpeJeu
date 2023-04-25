package com.example.poulpejeu


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.ComponentActivity
import android.widget.LinearLayout
import android.widget.TextView
import kotlin.math.roundToInt


class BridgeGame : ComponentActivity() {
    private var currentRow = 8
    private var dead = 0
    private lateinit var data: Array<BooleanArray>
    private lateinit var myTextView: TextView
    private lateinit var buttons: Array<Array<GlassButton>>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bridgegame_layout)

        myTextView = findViewById(R.id.dead)
        myTextView.setText("dead : " + dead)


        var tab = IntArray(9)
        for (i in 0..8) {
            tab[i] = Math.random().roundToInt()
        }
// Tableau de données pour chaque ligne de boutons (1 = bouton correct, 0 = bouton incorrect)
        data = arrayOf(
            booleanArrayOf(tab[0] == 0, tab[0] == 1),
            booleanArrayOf(tab[1] == 0, tab[1] == 1),
            booleanArrayOf(tab[2] == 0, tab[2] == 1),
            booleanArrayOf(tab[3] == 0, tab[3] == 1),
            booleanArrayOf(tab[4] == 0, tab[4] == 1),
            booleanArrayOf(tab[5] == 0, tab[5] == 1),
            booleanArrayOf(tab[6] == 0, tab[6] == 1),
            booleanArrayOf(tab[7] == 0, tab[7] == 1),
            booleanArrayOf(tab[8] == 0, tab[8] == 1),

            )


        buttons = arrayOf<Array<GlassButton>>(
            arrayOf(findViewById(R.id.button0_0), findViewById(R.id.button0_1)),
            arrayOf(findViewById(R.id.button1_0), findViewById(R.id.button1_1)),
            arrayOf(findViewById(R.id.button2_0), findViewById(R.id.button2_1)),
            arrayOf(findViewById(R.id.button3_0), findViewById(R.id.button3_1)),
            arrayOf(findViewById(R.id.button4_0), findViewById(R.id.button4_1)),
            arrayOf(findViewById(R.id.button5_0), findViewById(R.id.button5_1)),
            arrayOf(findViewById(R.id.button6_0), findViewById(R.id.button6_1)),
            arrayOf(findViewById(R.id.button7_0), findViewById(R.id.button7_1)),
            arrayOf(findViewById(R.id.button8_0), findViewById(R.id.button8_1))
        )



        for (i in buttons.indices) {
            for (j in 0..1) {
                buttons[i][j].init(data[i][j])
                if (i == 8) {
                    buttons[i][j].active()
                }
                buttons[i][j].setOnClickListener {
                    onClick(i, j)
                }
            }

        }
    }


    private fun onClick(i: Int, j: Int) {
        if (i == currentRow) {

            if (buttons[i][j].discover()) {

                if (currentRow == 0) {
                    finish()
                } else {

                    buttons[i - 1][0].active()
                    buttons[i - 1][1].active()

                    currentRow--
                }

            } else {
                dead++;
                myTextView.setText("dead : " + dead)
                reInit()
            }
        }

    }


    private fun reInit() {
        currentRow = 8;
        for (i in buttons.indices) {
            if (i < 8) {
                buttons[i][0].reInit()
                buttons[i][1].reInit()
            }
        }
    }
}