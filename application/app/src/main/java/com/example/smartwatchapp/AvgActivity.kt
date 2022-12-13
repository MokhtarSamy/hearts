package com.example.smartwatchapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton


class AvgActivity : MyActivity() {

    override var stat = "avg"
    override var id = R.id.textViewAvg
    override var idComment = R.id.commentAvg


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_avg);

        getData()

        //bouton pour aller en arriere
        val buttonToMain = findViewById<ImageButton>(R.id.toMaxButton)
        buttonToMain.setOnClickListener {
            val intent = Intent(this,MaxActivity::class.java)
            startActivity(intent)
        }
    }
}