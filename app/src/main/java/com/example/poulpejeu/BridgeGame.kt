package com.example.poulpejeu


import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.ComponentActivity
import android.widget.LinearLayout
import android.widget.TextView


class BridgeGame : ComponentActivity(){
    private var currentRow = 8
    private var dead = 0
    private lateinit var data: Array<IntArray>
    private lateinit var broke: BooleanArray
    private lateinit var myTextView: TextView
    private lateinit var buttonsLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bridgegame_layout)
        Log.v("log", "caca")

        myTextView = findViewById<TextView>(R.id.dead)
        myTextView.setText("dead : " + dead)

// Tableau de données pour chaque ligne de boutons (1 = bouton correct, 0 = bouton incorrect)
        data = arrayOf(
            intArrayOf(1, 0),
            intArrayOf(0, 1),
            intArrayOf(1, 0),
            intArrayOf(0, 1),
            intArrayOf(1, 0),
            intArrayOf(0, 1),
            intArrayOf(1, 0),
            intArrayOf(0, 1),
            intArrayOf(1, 0)
        )

        broke = BooleanArray(9){false}
// Créez une vue LinearLayout pour contenir les boutons
        buttonsLayout = findViewById<LinearLayout>(R.id.buttons)
        buttonsLayout.orientation = LinearLayout.VERTICAL

// Boucle pour créer chaque ligne de boutons
        for (i in data.indices) {
            val row = LinearLayout(this)
            row.orientation = LinearLayout.HORIZONTAL

            // Boucle pour créer chaque bouton dans la ligne
            for (j in 0..1) {
                val button = Button(this)
                button.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
                ) // Les boutons s'étendent également pour remplir l'espace disponible
                button.text = if (data[i][j] == 1) "Correct" else "Incorrect"
                if(i < 8){
                    button.isEnabled = false
                }
                button.setOnClickListener{
                    onClick(i,j,button)
                }
                row.addView(button)
            }

            // Ajoutez la ligne de boutons à la vue LinearLayout
            buttonsLayout.addView(row)
        }
    }

    private fun onClick(i:Int, j:Int, button:Button){
        if ( i == 0 && data[i][j] == 1){
            finish()
        }
        else {
            if (data[i][j] == 1) {
                val previousRow = buttonsLayout.getChildAt(i - 1) as LinearLayout
                if(i == currentRow) {
                    if(broke[i-1]){
                        if(data[i-1][0] == 1){
                            previousRow.getChildAt(0).isEnabled = true
                        }
                        else{
                            previousRow.getChildAt(1).isEnabled = true
                        }
                    }
                        else {
                            previousRow.getChildAt(0).isEnabled = true
                            previousRow.getChildAt(1).isEnabled = true
                    }
                    currentRow--;
                }
            } else {
                dead++;
                myTextView.setText("dead : " + dead)
                button.isEnabled = false
                broke[i]=true;
                reInit()
            }
        }
    }

    private fun reInit(){
        currentRow = 8;
        for (i in data.indices) {
            if (i < 8){
                val cursor = buttonsLayout.getChildAt(i) as LinearLayout
                cursor.getChildAt(0).isEnabled = false
                cursor.getChildAt(1).isEnabled = false
            }
        }
    }
}