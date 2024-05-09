package com.example.mindmap

import android.content.Intent
import android.os.Bundle
import android.widget.GridView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var gridView: GridView
    private lateinit var scoreTextView: TextView
    private lateinit var imageAdapter: ImageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gridView = findViewById(R.id.grid_view)
        scoreTextView = findViewById(R.id.score_text_view)

        // Create and set up the ImageAdapter
        imageAdapter = ImageAdapter(this)
        gridView.adapter = imageAdapter

        // Set up a listener to update the score and check for game over
        imageAdapter.scoreChangeListener = { score ->
            updateScore(score)
            if (imageAdapter.allImagesRevealed()) {
                // Game over, navigate to EndPage and pass the score
                val intent = Intent(this, EndPage::class.java)
                intent.putExtra("GAME_SCORE", score)
                startActivity(intent)
                finish()
            }
        }

    }


    private fun updateScore(score: Int) {
        scoreTextView.text = "Score: $score"
    }
}
