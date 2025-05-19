package com.qwen.flashcardapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.qwen.flashcardapp.R

class ReviewActivity : AppCompatActivity() {

    private lateinit var flashcards: List<QuizActivity.Flashcard>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)

        flashcards = intent.getParcelableArrayListExtra("flashcards") ?: emptyList()
        Log.d("ReviewActivity", "Displaying ${flashcards.size} flashcards")

        val reviewContainer = findViewById<LinearLayout>(R.id.reviewContainer)
        reviewContainer.removeAllViews()
        // Inside onCreate()
        val backFab = findViewById<FloatingActionButton>(R.id.backToMainFab)
        backFab.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        flashcards.forEachIndexed { index, flashcard ->
            // Create card wrapper
            val card = MaterialCardView(this).apply {
                layoutParams = ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(16.dpToPx(), 12.dpToPx(), 16.dpToPx(), 12.dpToPx())
                }
                radius = 20f
                cardElevation = 8f
                setCardBackgroundColor(Color.WHITE)
                strokeWidth = 2
                strokeColor = Color.LTGRAY
                preventCornerOverlap = true
                useCompatPadding = true
            }

            // Inner layout for question and answer
            val innerLayout = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(24.dpToPx(), 24.dpToPx(), 24.dpToPx(), 24.dpToPx())
            }

            // Question TextView
            val questionView = TextView(this).apply {
                text = "Q${index + 1}: ${flashcard.question}"
                textSize = 18f
                setTextColor(Color.DKGRAY)
            }

            // checks if user got a question correct and assigns check or a cross
            //changes text color to red (wrong) or green (correct)
            val correct = flashcard.answer == flashcard.userAnswer
            val answerView = TextView(this).apply {
                text = " ${if (correct) "✅"  else "❌"} Correct Answer is: ${if (correct) "True" else "False"}"
                textSize = 16f
                setTextColor(if (correct) Color.parseColor("#388E3C") else Color.parseColor("#D32F2F"))
                setPadding(0, 8.dpToPx(), 0, 0)
            }

            innerLayout.addView(questionView)
            innerLayout.addView(answerView)
            card.addView(innerLayout)
            reviewContainer.addView(card)
        }
    }

    private fun Int.dpToPx(): Int = (this * resources.displayMetrics.density).toInt()
}
