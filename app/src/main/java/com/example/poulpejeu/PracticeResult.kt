package com.example.poulpejeu


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AlertDialog
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson
import kotlin.math.log

class PracticeResult : ComponentActivity() {
    private lateinit var score:TextView
    private lateinit var back:Button
    private lateinit var scoreList: ListView
    private var adapter: ScoreAdapter? = null

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

        val sharedPreferences = getSharedPreferences("scores", MODE_PRIVATE)
        val lastScore = sharedPreferences.getInt("lastscore",0)
        val lastGame = intent.getStringExtra("game")

        if(lastGame=="Quiz") {
            val scores = addScore(lastGame!!, lastScore, sharedPreferences, this)
            adapter = ScoreAdapter(this, R.layout.score_listview, scores)
            scoreList.adapter = adapter
        }





        back.setOnClickListener { finish() }
    }

    fun addScore(game: String, score: Int, preferences: SharedPreferences,context: Context) : MutableList<Pair<String, Int>> {
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

        for (i in 0 until scores.size ){
            scores[i] = Pair(scores[i].first, scores[i].second.toInt())
        }

        // Vérifier si le score est suffisamment élevé pour demander le pseudo du joueur
        if (scores.size<5 || score > scores.lastOrNull()?.second ?: 0) {
            // Créer une vue pour la fenêtre popup
            val popupView = LayoutInflater.from(context).inflate(R.layout.addpseudo_popup, null)

            // Trouver les éléments de la vue
            val input = popupView.findViewById<EditText>(R.id.edittext_pseudo)
            val submitButton = popupView.findViewById<Button>(R.id.button_save)

            // Créer la fenêtre popup
            val popup = AlertDialog.Builder(context)
                .setView(popupView)
                .create()

            // Ajouter un listener pour le bouton de soumission
            submitButton.setOnClickListener {
                // Récupérer le pseudo saisi
                val playerName = input.text.toString()

                // Ajouter le nouveau score avec le pseudo saisi
                Log.i("zizi",scores.toString())
                scores.add(Pair(playerName, score))
                Log.i("pipi",scores.toString())

                scores.sortByDescending { it.second }
                if (scores.size > 5) {
                    scores.removeLast()
                }

                // Enregistrer les scores mis à jour dans les SharedPreferences
                val json = Gson().toJson(scores)
                Log.i("json",json.toString())
                preferences.edit().putString(game, json).apply()

                adapter?.notifyDataSetChanged()

                // Fermer la fenêtre popup
                popup.dismiss()
            }

            // Afficher la fenêtre popup
            popup.show()
        } else {
            // Enregistrer les scores dans les SharedPreferences sans demander le pseudo du joueur
            val json = Gson().toJson(scores)
            Log.i("json",json.toString())
            preferences.edit().putString(game, json).apply()
        }
        Log.i(scores.toString(),"hahaha")
        return scores
    }






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

    private fun updateScoreList(scores: MutableList<Pair<String, Int>>) {
        if (adapter == null) {
            adapter = ScoreAdapter(this, R.layout.score_listview, scores)
            scoreList.adapter = adapter
        } else {
            adapter?.clear()
            adapter?.addAll(scores)
            adapter?.notifyDataSetChanged()
        }
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

