package com.qwen.flashcardapp



import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

import android.os.Parcelable
import com.qwen.flashcardapp.R



class ResultActivity : AppCompatActivity() {

    private lateinit var scoreTextView: TextView

    private  lateinit var  scoreValue:TextView
    private lateinit var feedbackTextView: TextView
    private lateinit var reviewButton: Button
    private lateinit var exitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        scoreTextView = findViewById(R.id.scoreTextView)
        scoreValue =  findViewById(R.id.scoreValueTextView)
        feedbackTextView = findViewById(R.id.feedbackTextView)
        reviewButton = findViewById(R.id.reviewButton)
        exitButton = findViewById(R.id.exitButton)


        val score = intent.getIntExtra("score", 0)
        val total = intent.getIntExtra("total", 0)

        scoreTextView.text = "You got $score out of $total correct"
        scoreValue.text ="$score/$total"

        feedbackTextView.text = when {
            score == total -> "Perfect score! 🏆 You’re a history master!"
            score >= total * 0.7 -> "Well done! You're on your way to greatness."
            score >= total * 0.4 -> "Not bad! A little more reading and you'll get there."
            else -> "Keep going! Every expert was once a beginner."
        }

        reviewButton.setOnClickListener {
            val intent = Intent(this, ReviewActivity::class.java).apply {
                // Ensure you're passing the SAME flashcards list that QuizActivity sent
                putParcelableArrayListExtra("flashcards", intent.getParcelableArrayListExtra("flashcards"))
            }
            startActivity(intent)
        }

        exitButton.setOnClickListener {
            finishAffinity() // This exits the app completely
        }
    }
}
