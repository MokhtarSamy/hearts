package com.example.smartwatchapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class MaxActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_max)

        //bouton pour aller en arriere
        val buttonToMain = findViewById<ImageButton>(R.id.toMinButton)
        buttonToMain.setOnClickListener {
            val intent = Intent(this,MinActivity::class.java)
            startActivity(intent)
        }
    }
}