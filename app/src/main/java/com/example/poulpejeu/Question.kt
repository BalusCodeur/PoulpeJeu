package com.example.poulpejeu

class Question(val question: String, var options: List<String>, val answer: String) {


    fun shuffleOptions() {
        options = options.shuffled()
    }

}