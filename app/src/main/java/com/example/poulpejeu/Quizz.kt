package com.example.poulpejeu

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity


class Quizz : ComponentActivity() {

    private lateinit var questionTextView: TextView
    private lateinit var result: TextView
    private lateinit var button1: Button
    private lateinit var button2: Button
    private lateinit var button3: Button

    private var questions = listOf<Question>()
    private var currentQuestionIndex = 0
    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quizz_layout)

        // Initialize UI elements
        questionTextView = findViewById(R.id.question)
        button1 = findViewById(R.id.repButton1)
        button2 = findViewById(R.id.repButton2)
        button3 = findViewById(R.id.repButton3)

        // Initialize question list
        questions  = Question.allQuestions.shuffled().take(5)

        // Shuffle options for each question
        questions.forEach { it.shuffleOptions() }

        // Initialize first question
        setQuestion(questions[currentQuestionIndex])
    }

    private fun setQuestion(question: Question) {
        // Update question text and answer options
        questionTextView.text = question.question
        button1.text = question.options[0]
        button2.text = question.options[1]
        button3.text = question.options[2]

        // Reset answer buttons' background colors
        button1.setBackgroundResource(android.R.drawable.btn_default)
        button2.setBackgroundResource(android.R.drawable.btn_default)
        button3.setBackgroundResource(android.R.drawable.btn_default)

        // Set click listeners for answer buttons
        button1.setOnClickListener {
            if (isAnsweredCorrectly(button1.text.toString())) {
                button1.setBackgroundColor(Color.GREEN)
                score++
            } else {
                button1.setBackgroundColor(Color.RED)
            }
            goToNextQuestion()
        }
        button2.setOnClickListener {
            if (isAnsweredCorrectly(button2.text.toString())) {
                button2.setBackgroundColor(Color.GREEN)
                score++
            } else {
                button2.setBackgroundColor(Color.RED)
            }
            goToNextQuestion()
        }
        button3.setOnClickListener {
            if (isAnsweredCorrectly(button3.text.toString())) {
                button3.setBackgroundColor(Color.GREEN)
                score++
            } else {
                button3.setBackgroundColor(Color.RED)
            }
            goToNextQuestion()
        }
    }

    private fun isAnsweredCorrectly(answer: String): Boolean {
        return questions[currentQuestionIndex].answer == answer
    }

    private fun goToNextQuestion() {
        // Delay switching to the next question by 1 second for visual feedback
        window.decorView.postDelayed({
            // Increment current question index
            currentQuestionIndex++

            // If all questions have been answered, show score and reset the quiz
            if (currentQuestionIndex == questions.size) {
                showScore()
                resetQuiz()
            } else {
                // Otherwise, show the next question
                setQuestion(questions[currentQuestionIndex])
            }
        }, 1000)
    }


            fun showScore() {
            // Show score in a toast message
            val score = "$score/${questions.size}";
                val intent = Intent(this, Result::class.java)
                intent.putExtra("score",score)
                startActivity(intent)
        }

            fun resetQuiz() {
            // Reset current question index and score
            currentQuestionIndex = 0
            score = 0

            // Shuffle options for each question
            questions.forEach { it.shuffleOptions() }

            // Initialize first question
            setQuestion(questions[currentQuestionIndex])
        }

    }