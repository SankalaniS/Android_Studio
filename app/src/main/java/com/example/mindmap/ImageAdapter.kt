package com.example.mindmap

import android.content.Context
import android.content.SharedPreferences
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageView
import android.widget.Toast

class ImageAdapter(private val context: Context) : BaseAdapter() {

    private val imageIds = listOf(
        R.drawable.img1, R.drawable.img3, R.drawable.img2,
        R.drawable.img2, R.drawable.img2, R.drawable.img2,
        R.drawable.img3, R.drawable.img1, R.drawable.img3,
        R.drawable.img1, R.drawable.img1, R.drawable.img3,
        R.drawable.img2, R.drawable.img2, R.drawable.img1,
        R.drawable.img1, R.drawable.img3, R.drawable.img3
    ).shuffled()

    private val revealedImages = arrayOfNulls<Int>(imageIds.size)
    private var lastClickedPosition = -1
    private var pairsFound = 0

    private var score = 0
    private var attempts = 0
    private var highScore = 0


    //store and retrieve the highest score
    private val sharedPrefs: SharedPreferences by lazy {
        context.getSharedPreferences("HighScorePrefs", Context.MODE_PRIVATE)
    }

    var scoreChangeListener: ((Int) -> Unit)? = null
    var highScoreChangeListener: ((Int) -> Unit)? = null

    init {
        highScore = sharedPrefs.getInt("HighScore", 0)
        highScoreChangeListener?.invoke(highScore)
    }

    override fun getCount(): Int {
        return imageIds.size
    }

    override fun getItem(position: Int): Any {
        return imageIds[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val imageView: ImageView
        if (convertView == null) {
            imageView = ImageView(context)
            imageView.layoutParams = ViewGroup.LayoutParams(200, 200) // Change here
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            imageView.setPadding(8, 8, 8, 8)
            imageView.setOnClickListener {
                revealImage(position, imageView)
            }
        } else {
            imageView = convertView as ImageView
        }

        if (revealedImages[position] != null) {
            imageView.setImageResource(revealedImages[position]!!)
        } else {
            imageView.setImageResource(R.drawable.hidden_image)
        }

        return imageView
    }

    private fun updateScore(newScore: Int) {
        score = newScore
        scoreChangeListener?.invoke(score)

        if (score > highScore) {
            highScore = score
            highScoreChangeListener?.invoke(highScore)

            // Store new high score in SharedPreferences
            sharedPrefs.edit().putInt("HighScore", highScore).apply()
        }
    }
    //total number of images in the game
    fun countImages(): Int {
        return imageIds.size
    }
    //checks all images in the game have been revealed
    fun allImagesRevealed(): Boolean {
        return revealedImages.none { it == null }
    }

    private fun revealImage(position: Int, imageView: ImageView) {
        if (revealedImages[position] != null) {
            return // Image already revealed, do nothing
        }

        imageView.setImageResource(imageIds[position])

        if (lastClickedPosition == -1) {
            lastClickedPosition = position
        } else {
            if (imageIds[lastClickedPosition] == imageIds[position]) {
                // Match found
                Toast.makeText(context, "Match found!", Toast.LENGTH_SHORT).show()
                revealedImages[lastClickedPosition] = imageIds[lastClickedPosition]
                revealedImages[position] = imageIds[position]
                pairsFound++
                updateScore(score + 100)// Increment score for a match
                if (pairsFound == imageIds.size / 2) {
                    Toast.makeText(context, "Congratulations! You've won!", Toast.LENGTH_LONG).show()

                }
            } else {
                // No match, hide both images after a short delay
                val prevImageView = (imageView.parent as GridView).getChildAt(lastClickedPosition) as ImageView
                imageView.postDelayed({
                    prevImageView.setImageResource(R.drawable.hidden_image)
                    imageView.setImageResource(R.drawable.hidden_image)
                }, 1000)
                // No match
                // Adjust scoring as needed
                updateScore(score - 50)// Deduct points for an incorrect attempt
                Toast.makeText(context, "No match. Score: $score", Toast.LENGTH_SHORT).show()

            }
            lastClickedPosition = -1
        }

    }
}
