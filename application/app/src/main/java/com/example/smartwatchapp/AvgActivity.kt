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

class AvgActivity : AppCompatActivity() {

    lateinit var newImage: ImageView
    lateinit var newText : TextView

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

    private fun addTextView() {
        newText = TextView(this)
        findViewById<ConstraintLayout>(R.id.layout).addView(newText)

        newText.layoutParams.height = 50
        newText.layoutParams.width = 175
        newText.x = 110F
        newText.y = 250F
        newText.setText("testabcdefghijklmnopqrstuvwxyz")

    }

    private fun readData(){

        FirebaseUtils().fireStoreDatabase.collection("stats")
            .get()
            .addOnSuccessListener { querySnapshot ->
                querySnapshot.forEach { document ->
                    Log.d(TAG, "Read document with ID ${document.id}")
                    Log.d(TAG, document.get("avg").toString())
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents $exception")
            }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_avg);

        addImageView()
        addTextView()

        //bouton pour aller en arriere
        val buttonToMain = findViewById<ImageButton>(R.id.toMaxButton)
        buttonToMain.setOnClickListener {
            val intent = Intent(this,MaxActivity::class.java)
            startActivity(intent)
        }
    }
}