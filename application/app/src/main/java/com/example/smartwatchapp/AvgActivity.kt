package com.example.smartwatchapp

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import java.lang.Math.round
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.roundToInt
import kotlin.properties.Delegates



class AvgActivity : MyActivity() {

    override var stat = "avg"
    override var id = R.id.textViewAvg


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