package com.example.smartwatchapp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout

class DashboardOnSmartwatch : AppCompatActivity() {

    lateinit var newImage: ImageView
    lateinit var newText : TextView

    private fun addImageView() {
        newImage = ImageView(this)

        //Creation du carré contenant l'image view
        findViewById<ConstraintLayout>(R.id.layout).addView(newImage)
        newImage.layoutParams.height = 175
        newImage.layoutParams.width = 175
        newImage.x = 110F
        newImage.y = 80F

        //?
        newImage.setBackgroundColor(Color.MAGENTA) // A REVOIR
    }

    private fun addTextView() {
        newText = TextView(this)
        findViewById<ConstraintLayout>(R.id.layout).addView(newText)

        newText.layoutParams.height = 50
        newText.layoutParams.width = 175
        newText.x = 110F
        newText.y = 250F
        newText.setText("testabcdefghijklmnopqrstuvwxyz");

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_on_smartwatch);

        addImageView()
        addTextView()

        //bouton pour revenir en arriere
        val buttonToMain = findViewById<ImageButton>(R.id.toMainButton)
        buttonToMain.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }
}