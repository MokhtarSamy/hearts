package com.example.smartwatchapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton

class MaxActivity : AppCompatActivity() {

    private fun getData(){

        FirebaseUtils().fireStoreDatabase.collection("stats")
            .get()
            .addOnSuccessListener { querySnapshot ->
                querySnapshot.forEach { document ->
                    Log.d(TAG, "Read document with ID ${document.id}")
                    Log.d(TAG, "${document.get("max")}");
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents $exception")
            }

    }

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