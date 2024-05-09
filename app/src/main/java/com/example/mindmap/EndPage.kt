package com.example.mindmap

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class EndPage : AppCompatActivity() {

    private lateinit var gameScoreTextView: TextView
    private lateinit var highScoreTextView: TextView
    private var gameScore: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end_page)

        gameScore = intent.getIntExtra("GAME_SCORE", 0)

        gameScoreTextView = findViewById(R.id.game_score_text_view)
        highScoreTextView = findViewById(R.id.high_score_text_view)

        gameScoreTextView.text = "Game Score: $gameScore"

        val highScore = getHighScore()
        highScoreTextView.text = "High Score: $highScore"

        if (gameScore > highScore) {
            saveHighScore(gameScore)
            highScoreTextView.text = "New High Score: $gameScore"
        }

        val playAgainButton: Button = findViewById(R.id.button2)
        playAgainButton.setOnClickListener {
            // Navigate to MainActivity when "Play Again" button is clicked
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Optional, depending on your app's flow
        }
    }

    private fun getHighScore(): Int {
        val sharedPrefs: SharedPreferences =
            getSharedPreferences("HighScorePrefs", Context.MODE_PRIVATE)
        return sharedPrefs.getInt("HighScore", 0)
    }

    private fun saveHighScore(score: Int) {
        val sharedPrefs: SharedPreferences =
            getSharedPreferences("HighScorePrefs", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putInt("HighScore", score)
        editor.apply()
    }
}
