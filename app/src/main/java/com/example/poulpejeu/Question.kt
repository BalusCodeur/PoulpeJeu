package com.example.poulpejeu

class Question(val question: String, var options: List<String>, val answer: String) {


    fun shuffleOptions() {
        options = options.shuffled()
    }

    companion object {
        val allQuestions = listOf(
            Question(
                "Quel pays consomme le plus de fromage par habitant ?",
                listOf("L'italie", "La France", "La Finlande"),
                "La Finlande"
            ),
            Question(
                "Quel est l'aliment le plus volé ?",
                listOf("Le fromage", "La trotinette", "Les chips"),
                "Le fromage"
            ),
            Question(
                "Quel est l'age du plus vieux fromage du monde ?",
                listOf("340 ans", "1400 ans", "3200 ans"),
                "3200 ans"
            ),
            Question(
                "Comment s'appelle la phobie du fromage ?",
                listOf("La tyrophobie", "La fromagophobie", "La formaphobie"),
                "La tyrophobie"
            ),
            Question(
                "Combien de kg de fromages sont produits par seconde dans le monde ?",
                listOf("655 kg", "843 kg", "365 kg"),
                "655 kg"
            ),
            Question(
                "Le camembert est un formage à pate ... ?",
                listOf("Persillée", "Pressée non cuite", "Molle"),
                "Molle"
            ),
            Question(
                "Le camembert est un formage à pate ... ?",
                listOf("Persillée", "Pressée non cuite", "Molle"),
                "Molle"
            ),
            Question(
                "Quelle est l'origine de l'emmental ?",
                listOf("Italie", "France", "Suisse"),
                "Suisse"
            ),
            Question(
                "D'où provient le Trappiste ?",
                listOf("Bretagne", "Savoie", "Nord-Pas de Calais"),
                "Nord-Pas de Calais"
            ),
            Question(
                "En moyenne combien de kilogrammes de fromage consomme un français pendant 1 an ?",
                listOf("24,6 kg", "17,3 kg", "15,7 kg"),
                "24,6 kg"
            ),
            Question(
                "A combien de Kcal correspondent 100 grammes de Reblochon ?",
                listOf("178 Kcal", "354 Kcal", "465 Kcal"),
                "354 Kcal"
            ),




            // Ajouter d'autres questions ici
        )
    }


}
