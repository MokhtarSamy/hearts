package com.example.smartwatchapp

import android.graphics.Color
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

abstract class MyActivity() : AppCompatActivity() {

    lateinit var newText : TextView
    private var heartBeats = 0

    abstract var stat : String
    abstract var id : Int


    private fun getTime() : String {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val current = LocalDateTime.now().format(formatter)
        return current.toString()
    }

    fun getData() {

        FirebaseUtils().fireStoreDatabase.collection("stats")
            .whereEqualTo("date",getTime()).get()
            .addOnSuccessListener { querySnapshot ->
                querySnapshot.forEach { document ->
                    heartBeats = (document.get(stat) as Double).roundToInt();
                    setTextView(heartBeats)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents $exception")
            }

        Log.d(TAG,heartBeats.toString() )


    }

    private fun setTextView(heartBeats : Int ) {
        getData();
        newText = findViewById<TextView>(id)

        /*newText.layoutParams.height = 150
        newText.layoutParams.width = 175
        newText.x = 110F
        newText.y = 250F
        */
        newText.setTextColor(Color.WHITE);
        newText.textSize = 20.0f
        newText.text = heartBeats.toString()
        Log.d(TAG, newText.text.toString())
    }

    private fun comment(heartBeat : Int) : String {
        if ((heartBeat) < 40) {
            return "ATTENTION ! Votre rythme cardiaque est anormalement faible!"
        }
        if ((heartBeat) > 150) {
            return "ATTENTION ! Votre rythme cardiaque est anormalement élevé!"
        }
        return "Vous avez un rythme cardiaque acceptable."

    }



    private fun addImageView() {
        var newImage = ImageView(this)

        //Creation du carré contenant l'image view
        findViewById<ConstraintLayout>(R.id.layout).addView(newImage)
        newImage.layoutParams.height = 50
        newImage.layoutParams.width = 50
        newImage.x = 110F
        newImage.y = 80F

        //?
        newImage.setBackgroundColor(Color.MAGENTA) // A REVOIR
    }
}
