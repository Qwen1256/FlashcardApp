package com.qwen.flashcardapp


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.qwen.flashcardapp.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val welcomeTextView: TextView = findViewById(R.id.welcomeTextView)
        val descriptionTextView: TextView = findViewById(R.id.descriptionTextView)
        val startButton: Button = findViewById(R.id.startButton)

        welcomeTextView.text = "Welcome to History Flashcards!"
        descriptionTextView.text = "Test your knowledge with true or false questions on historical events. Ready to prove how much you know?"

        startButton.setOnClickListener {
            startButton.animate().scaleX(0.9f).scaleY(0.9f).setDuration(100)
                .withEndAction {
                    startButton.animate().scaleX(1f).scaleY(1f).setDuration(100)
                    startActivity(Intent(this, QuizActivity::class.java))
                }
        }
    }
}