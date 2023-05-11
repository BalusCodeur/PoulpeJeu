package com.example.poulpejeu


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.activity.ComponentActivity

class PracticeResult : ComponentActivity() {
    private lateinit var score:TextView
    private lateinit var back:Button
    private lateinit var scoreList: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.practice_result_layout)
        score = findViewById(R.id.score)
        back = findViewById(R.id.backButton)
        scoreList = findViewById(R.id.scoreList)

        // Récupérer le score depuis l'intent
        val scoreActivity = intent.getStringExtra("score")

        // Afficher le score dans le TextView
        score.text = scoreActivity



        /*when (intent.getStringExtra("game")){
            "quiz" ->{
                if (scoreActivity != null) {
                    //quizScore(scoreActivity)
                }
            }
            "biscuit" ->{}
            "soleil" ->{}
            "bridge" ->{}
        }*/

        val sharedPreferences = getSharedPreferences("scores", Context.MODE_PRIVATE)
        val lastScore = sharedPreferences.getInt("lastscore",0)
        val lastGame = intent.getStringExtra("game")
        val count = sharedPreferences.getInt("count", 0)
        var scoresList = mutableListOf<String>()

        if (lastGame != null) {
           scoresList= showBest(lastGame,lastScore,sharedPreferences)!!
        }

        // Récupérer les scores depuis les SharedPreferences et les ajouter à la liste
        //or (i in 1..count) {
        //    val score = sharedPreferences.getInt("score$i", 0)
        //    scoresList.add(score.toString())
        //}

        // Tri des scores en ordre décroissant
        //scoresList.sortDescending()

        // Afficher les 5 meilleurs scores dans une ListView
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, scoresList.take(5))
        scoreList.adapter = adapter

        // Ajouter le score actuel à la liste des scores
        if (scoreActivity != null) {
            scoresList.add(scoreActivity.split("/")[0])
        }

        // Enregistrer la nouvelle liste des scores dans les SharedPreferences
        //val scoresListString = scoresList.joinToString(",")
        //sharedPreferences.edit().putString("scoresList", scoresListString).apply()

        back.setOnClickListener { finish() }
    }





    /*fun quizScore(score: String){
        // Récupération des Shared Preferences pour le jeu 1
        val sharedPref = getSharedPreferences("quiz", Context.MODE_PRIVATE)
        val playerName = "John Doe"
        val score = 100
        val editor = sharedPref.edit()
        editor.putString(playerName, score.toString())
        editor.apply()
        val allScores = sharedPref.all
        for ((playerName, score) in allScores) {
            Log.d("MyGame", "$playerName: $score")
        }
        //scoreList.adapter = ScoreAdapter(this, allScores as Map<String, Int>)
        //val topScores = mutableListOf<Pair<String, Long>>()

        /*val scoreList = sharedPreferences.getStringSet("scoreList", setOf())?.map {
            val (score, pseudo) = it.split(",")
            score.toInt() to pseudo
        }?.toMutableList() ?: mutableListOf()*/
    }*/

    fun showBest(game: String, lastscore: Int,preferences: SharedPreferences) : MutableList<String>? {
        var scoreList = preferences.getString("score"+game,"")?.split(",")?.sortedDescending()?.toMutableList()
        val editor = preferences.edit()
        if (scoreList != null) {
            for (score in scoreList){
                if (lastscore.toString() > score){
                    Log.i("Actualisation du meilleur score","")
                    scoreList?.removeLast()
                    scoreList.add(lastscore.toString())
                    break
                }
            }
            scoreList = scoreList?.sortedDescending()?.toMutableList()
            editor.putString("score"+game,scoreList?.joinToString(","))
            editor.apply()
            //Log.i(scoreList?.joinToString(","),"OK")
        }

        return scoreList
    }

}



/*class ScoreAdapter(context: Context, scores: Map<String, Int>) : ArrayAdapter<String>(context, 0) {

    private val sortedScores = scores.toList().sortedByDescending { (_, value) -> value }.toMap()

    override fun getCount(): Int {
        return sortedScores.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, parent, false)
        val entry = sortedScores.entries.elementAt(position)
        view.findViewById<TextView>(android.R.id.text1).text = entry.key
        view.findViewById<TextView>(android.R.id.text2).text = entry.value.toString()
        return view
    }
}*/
