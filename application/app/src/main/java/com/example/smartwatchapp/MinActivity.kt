package com.example.smartwatchapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MinActivity : AppCompatActivity() {
    private fun getData(){

        FirebaseUtils().fireStoreDatabase.collection("stats")
            .get()
            .addOnSuccessListener { querySnapshot ->
                querySnapshot.forEach { document ->
                    Log.d(TAG, "Read document with ID ${document.id}")
                    Log.d(TAG, "${document.get("min")}");
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents $exception")
            }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_min)
    }
}