package com.example.poulpejeu


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson

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
        //var scoresList = mutableListOf<String>()


        /*if (lastGame != null) {
           scoresList= showBest(lastGame,lastScore,sharedPreferences)!!
        }*/

        val scores = addScore("test","Popeye5",3,sharedPreferences)
        val adapter = ScoreAdapter(this, R.layout.score_listview, scores)


        // Récupérer les scores depuis les SharedPreferences et les ajouter à la liste
        //or (i in 1..count) {
        //    val score = sharedPreferences.getInt("score$i", 0)
        //    scoresList.add(score.toString())
        //}

        // Tri des scores en ordre décroissant
        //scoresList.sortDescending()
        // Afficher les 5 meilleurs scores dans une ListView
        //val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, scoresList.take(5))
        scoreList.adapter = adapter

        // Ajouter le score actuel à la liste des scores
        //if (scoreActivity != null) {
        //    scoresList.add(scoreActivity.split("/")[0])
        //}

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
        }

        return scoreList
    }

    fun addScore(game: String, player: String, score: Int, preferences: SharedPreferences) : MutableList<Pair<String, Int>> {
        var scores = preferences.getString(game, null)?.let {
            Log.i(it, "Scores from SharedPreferences:")
            try {
                Gson().fromJson<List<Pair<String, Int>>>(it, object : TypeToken<List<Pair<String, Int>>>() {}.type).toMutableList()
            } catch (e: Exception) {
                Log.i("Boobybooby","")
                null
            }
        } ?: mutableListOf<Pair<String, Int>>()
        Log.i(scores.toString(),"1")
        scores[0] = Pair(scores[0].first, scores[0].second.toInt())

        for (i in 0 until scores.size ){
            scores[i] = Pair(scores[i].first, scores[i].second.toInt())
        }

        scores.add(Pair(player, score))
        Log.i(scores.toString(),"2")
        scores.sortByDescending { it.second }
        Log.i("3",scores.toString())
        if (scores.size > 5) {
            scores.removeLast()
        }
        val json = Gson().toJson(scores)
        Log.i("json",json.toString())
        preferences.edit().putString(game, json).apply()
        return scores
    }





}

class ScoreAdapter(context: Context, resource: Int, scores: List<Pair<String, Int>>) : ArrayAdapter<Pair<String, Int>>(context, resource, scores) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.score_listview, parent, false)
        val score = getItem(position)
        view.findViewById<TextView>(R.id.textview_pseudo).text = score?.first
        view.findViewById<TextView>(R.id.textview_score).text = score?.second.toString()
        return view
    }
}
