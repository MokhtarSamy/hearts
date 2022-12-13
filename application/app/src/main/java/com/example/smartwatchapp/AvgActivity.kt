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

class AvgActivity : AppCompatActivity() {

    lateinit var newImage: ImageView
    lateinit var newText : TextView
    private var heartBeatsAvg = 0;


    private fun getTime() : String {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val current = LocalDateTime.now().format(formatter)
        Log.d(TAG, current.toString())
        return current.toString()
    }

    private fun getData() {

        FirebaseUtils().fireStoreDatabase.collection("stats")
            .whereEqualTo("date",getTime()).get()
            .addOnSuccessListener { querySnapshot ->
                querySnapshot.forEach { document ->
                    Log.d(TAG, "Average is ${document.get("avg")}");
                    heartBeatsAvg = (document.get("avg") as Double).roundToInt();
                    setTextView(heartBeatsAvg.toString())
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents $exception")
            }

        Log.d(TAG,heartBeatsAvg.toString() )


    }

    private fun addImageView() {
        newImage = ImageView(this)

        //Creation du carré contenant l'image view
        findViewById<ConstraintLayout>(R.id.layout).addView(newImage)
        newImage.layoutParams.height = 50
        newImage.layoutParams.width = 50
        newImage.x = 110F
        newImage.y = 80F

        //?
        newImage.setBackgroundColor(Color.MAGENTA) // A REVOIR
    }

    private fun setTextView(heartBeats : String ) {
        getData();
        newText = findViewById<TextView>(R.id.textViewMax)

        /*newText.layoutParams.height = 150
        newText.layoutParams.width = 175
        newText.x = 110F
        newText.y = 250F
        */
        newText.setTextColor(Color.WHITE);
        newText.textSize = 20.0f
        newText.text = heartBeats
        Log.d(TAG, newText.text.toString())

    }

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