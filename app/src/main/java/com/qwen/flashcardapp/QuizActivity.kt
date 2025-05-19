package com.qwen.flashcardapp

import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

import com.qwen.flashcardapp.R
import com.qwen.flashcardapp.databinding.ActivityQuizBinding
import java.util.ArrayList

class QuizActivity : AppCompatActivity() {

    // Flashcard data class represents a single flashcard with a question and a true/false answer.
    // It implements Parcelable to allow passing instances between Android components.
    data class Flashcard(
        val question: String,
        val answer: Boolean,
        var userAnswer: Boolean? = null // nullable to track if user answered or not
    ) : Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readString()!!,
            parcel.readByte() != 0.toByte(),
            parcel.readByte().let {
                when (it) {
                    0.toByte() -> false
                    1.toByte() -> true
                    else -> null
                }
            }
        )

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(question)
            parcel.writeByte(if (answer) 1 else 0)
            parcel.writeByte(
                when (userAnswer) {
                    true -> 1
                    false -> 0
                    null -> 2 // use 2 to indicate null
                }
            )
        }

        override fun describeContents(): Int = 0

        companion object CREATOR : Parcelable.Creator<Flashcard> {
            override fun createFromParcel(parcel: Parcel): Flashcard {
                return Flashcard(parcel)
            }

            override fun newArray(size: Int): Array<Flashcard?> {
                return arrayOfNulls(size)
            }
        }
    }

    private lateinit var binding: ActivityQuizBinding

    private val flashcards = listOf(
        Flashcard("The Declaration of Independence was signed in 1776.", true),
        Flashcard("The Berlin Wall fell in 1989.", true),
        Flashcard("The Roman Empire fell in the 12th century.", false),
        Flashcard("Napoleon Bonaparte was exiled to Elba before returning to power.", true),
        Flashcard("The Cold War was a direct military conflict between the US and USSR.", false)
    )

    private var currentIndex = 0
    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setQuestion()

        binding.trueButton.setOnClickListener {
            checkAnswer(true)

        }

        binding.falseButton.setOnClickListener {
            checkAnswer(false)
        }

        binding.nextButton.setOnClickListener {
            binding.feedbackTextView.visibility = View.GONE
            currentIndex++
            if (currentIndex < flashcards.size) {
                setQuestion()
                binding.feedbackTextView.text = ""

            } else {
                val intent = Intent(this, ResultActivity::class.java)
                intent.putExtra("score", score)
                intent.putExtra("total", flashcards.size)
                intent.putParcelableArrayListExtra("flashcards", ArrayList(flashcards))
                startActivity(intent)
                finish()
            }
        }
    }

    private fun setQuestion() {
        binding.questionTextView.text = flashcards[currentIndex].question
        binding.trueButton.isEnabled = true
        binding.falseButton.isEnabled = true
        binding.nextButton.isEnabled = false
        updateProgress()
    }

    // This function checks whether the user's answer matches the correct answer,
    // updates the UI with feedback, and adjusts button states accordingly.
    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = flashcards[currentIndex].answer
        flashcards[currentIndex].userAnswer = userAnswer
        if (userAnswer == correctAnswer) {
            // User answered correctly
            binding.feedbackTextView.text = "Correct!"

            binding.feedbackTextView.setTextColor(ContextCompat.getColor(this, R.color.correct_green))
            binding.feedbackTextView.visibility = View.VISIBLE
            score++
        } else {
            // User answered incorrectly
            binding.feedbackTextView.text = "Incorrect."
            binding.feedbackTextView.setTextColor(ContextCompat.getColor(this, R.color.red_500))
            binding.feedbackTextView.visibility = View.VISIBLE
        }
        binding.trueButton.isEnabled = false
        binding.falseButton.isEnabled = false
        binding.nextButton.isEnabled = true
    }

    private fun updateProgress() {
        val progress = ((currentIndex + 1).toFloat() / flashcards.size * 100).toInt()
        ObjectAnimator.ofInt(binding.progressBar, "progress", progress)
            .setDuration(500)
            .start()
    }


}
