package com.example.poulpejeu.menus


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AlertDialog
import com.example.poulpejeu.R
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson
import kotlin.math.roundToInt

class PracticeResult : ComponentActivity() {
    private lateinit var score:TextView
    private lateinit var back:Button
    private lateinit var scoreList: ListView
    private lateinit var title: ImageView
    private var adapterint: ScoreAdapterInt? = null
    private var adapterlong: ScoreAdapterLong? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.practice_result_layout)
        score = findViewById(R.id.score)
        back = findViewById(R.id.backButton)
        scoreList = findViewById(R.id.scoreList)
        title = findViewById(R.id.score_title)

        title.setImageResource(R.drawable.score)

        // Récupérer le score depuis l'intent
        val scoreActivity = intent.getStringExtra("score")

        // Afficher le score dans le TextView
        score.text = scoreActivity

        val sharedPreferences = getSharedPreferences("scores", MODE_PRIVATE)
        val lastScoreint = sharedPreferences.getInt("lastscoreint",0)
        val lastScorelong = sharedPreferences.getLong("lastscorelong",1000)
        val lastGame = intent.getStringExtra("game")

        if(lastGame=="Quiz"|| lastGame=="Bridge") {
            val scores = addScore(lastGame!!, lastScoreint, sharedPreferences, this)
            adapterint = ScoreAdapterInt(this, R.layout.score_listview, scores)
            scoreList.adapter = adapterint
        }
        if(lastGame=="Biscuit" || lastGame=="Rope"|| lastGame=="Shout"|| lastGame == "Soleil"){
            val scores = addScore2(lastGame!!, lastScorelong, sharedPreferences, this)
            adapterlong = ScoreAdapterLong(this,R.layout.score_listview,scores,lastGame)
            scoreList.adapter = adapterlong
        }
        back.setOnClickListener { finish() }
    }

    fun addScore(game: String, score: Int, preferences: SharedPreferences,context: Context) : MutableList<Pair<String, Int>> {
        var scores = preferences.getString(game, null)?.let {
            Log.i(it, "Scores from SharedPreferences:")
            try {
                Gson().fromJson<List<Pair<String, Int>>>(it, object : TypeToken<List<Pair<String, Int>>>() {}.type).toMutableList()
            } catch (e: Exception) {
                null
            }
        } ?: mutableListOf<Pair<String, Int>>()

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
                scores.add(Pair(playerName, score))

                scores.sortByDescending { it.second }
                if (scores.size > 5) {
                    scores.removeLast()
                }

                // Enregistrer les scores mis à jour dans les SharedPreferences
                val json = Gson().toJson(scores)
                preferences.edit().putString(game, json).apply()

                adapterint?.notifyDataSetChanged()

                // Fermer la fenêtre popup
                popup.dismiss()
            }

            // Afficher la fenêtre popup
            popup.show()
        } else {
            // Enregistrer les scores dans les SharedPreferences sans demander le pseudo du joueur
            val json = Gson().toJson(scores)
            preferences.edit().putString(game, json).apply()
        }
        return scores
    }

    fun addScore2(game: String, score: Long, preferences: SharedPreferences,context: Context) : MutableList<Pair<String, Long>> {
        var scores = preferences.getString(game, null)?.let {
            Log.i(it, "Scores from SharedPreferences:")
            try {
                Gson().fromJson<List<Pair<String, Long>>>(it, object : TypeToken<List<Pair<String, Long>>>() {}.type).toMutableList()
            } catch (e: Exception) {
                Log.i("Boobybooby","")
                null
            }
        } ?: mutableListOf<Pair<String, Long>>()
        Log.i(scores.toString(),"1")

        for (i in 0 until scores.size ){
            scores[i] = Pair(scores[i].first, scores[i].second.toLong())
        }
        if (game == "Biscuit" || game == "Soleil"){
        // Vérifier si le score est suffisamment élevé pour demander le pseudo du joueur
        if (scores.size<5 || score < scores.lastOrNull()?.second ?: 0) {
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
                Log.i("zizi", scores.toString())
                scores.add(Pair(playerName, score))
                Log.i("pipi", scores.toString())

                scores.sortBy { it.second }
                if (scores.size > 5) {
                    scores.removeLast()
                }

                // Enregistrer les scores mis à jour dans les SharedPreferences
                val json = Gson().toJson(scores)
                Log.i("json", json.toString())
                preferences.edit().putString(game, json).apply()

                adapterlong?.notifyDataSetChanged()

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
        } else {
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
                    Log.i("zizi", scores.toString())
                    scores.add(Pair(playerName, score))
                    Log.i("pipi", scores.toString())

                    scores.sortByDescending { it.second }
                    if (scores.size > 5) {
                        scores.removeLast()
                    }

                    // Enregistrer les scores mis à jour dans les SharedPreferences
                    val json = Gson().toJson(scores)
                    Log.i("json", json.toString())
                    preferences.edit().putString(game, json).apply()

                    adapterlong?.notifyDataSetChanged()

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
        }
        Log.i(scores.toString(),"hahaha")
        return scores
    }


}

class ScoreAdapterInt(context: Context, resource: Int, scores: MutableList<Pair<String, Int>>) : ArrayAdapter<Pair<String, Int>>(context, resource, scores) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.score_listview, parent, false)
        val score = getItem(position)
        view.findViewById<TextView>(R.id.textview_pseudo).text = score?.first
        view.findViewById<TextView>(R.id.textview_score).text = score?.second.toString()
        return view
    }
}

class ScoreAdapterLong(context: Context, resource: Int, scores: MutableList<Pair<String, Long>>,game: String) : ArrayAdapter<Pair<String, Long>>(context, resource, scores) {
    var game = game
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.score_listview, parent, false)
        val score = getItem(position)
        view.findViewById<TextView>(R.id.textview_pseudo).text = score?.first
        //Log.i("game",game)
        when(game){
            "Biscuit" -> view.findViewById<TextView>(R.id.textview_score).text = (score?.second?.div(1000)).toString()+","+ (score?.second?.rem(1000)) + "s"
            "Rope" -> view.findViewById<TextView>(R.id.textview_score).text = (((score?.second?.times(
                10.0
            ))?.roundToInt() ?: 0) /10.0).toString() + " cm"
            "Soleil" -> view.findViewById<TextView>(R.id.textview_score).text = (score?.second).toString()+ "  s"
            "Shout" ->  view.findViewById<TextView>(R.id.textview_score).text = (((score?.second?.times(
                100.0
            ))?.roundToInt() ?: 0) /100.0).toString() + " dB"

        }

        return view
    }
}

