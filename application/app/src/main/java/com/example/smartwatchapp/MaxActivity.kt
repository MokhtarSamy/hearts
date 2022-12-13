package com.example.smartwatchapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton


class MaxActivity() : MyActivity() {
    override var stat = "max"
    override var id = R.id.textViewMax
    override var idComment = R.id.commentMax

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_max)

        getData()

        //bouton pour aller en arriere
        val buttonToMain = findViewById<ImageButton>(R.id.toMinButton)
        buttonToMain.setOnClickListener {
            val intent = Intent(this,MinActivity::class.java)
            startActivity(intent)
        }
    }
}