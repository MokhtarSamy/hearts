package com.example.smartwatchapp

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.smartwatchapp.databinding.ActivityMainBinding
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

private lateinit var analytics: FirebaseAnalytics
const val TAG = "FIRESTORE"

class MainActivity : Activity() {
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        analytics = Firebase.analytics
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        uploadData()

        // we'll call functions here
    }

    private fun uploadData() {
        binding!!.btnUploadData.setOnClickListener {

            // create a dummy data
            val hashMap = hashMapOf<String, Any>(
                "name" to "John doe",
                "city" to "Nairobi",
                "age" to 24
            )

            // use the add() method to create a document inside users collection
            FirebaseUtils().fireStoreDatabase.collection("users")
                .add(hashMap)
                .addOnSuccessListener {
                    Log.d(TAG, "Added document with ID ${it.id}")
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error adding document $exception")
                }
        }


    }

    private fun readData(){
        binding!!.btnReadData.setOnClickListener {
            FirebaseUtils().fireStoreDatabase.collection("users")
                .get()
                .addOnSuccessListener { querySnapshot ->
                    querySnapshot.forEach { document ->
                        Log.d(TAG, "Read document with ID ${document.id}")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents $exception")
                }
        }
    }
}



