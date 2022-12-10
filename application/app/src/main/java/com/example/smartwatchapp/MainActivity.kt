/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.smartwatchapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.smartwatchapp.databinding.ActivityMainBinding
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi



private lateinit var analytics: FirebaseAnalytics

/**
 * Activity displaying the app UI. Notably, this binds data from [MainViewModel] to views on screen,
 * and performs the permission check when enabling measure data.
 */

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    private val viewModel: MainViewModel by viewModels()

    @SuppressLint("WrongViewCast")
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)




        analytics = Firebase.analytics
        uploadData()
        readData()
        setContentView(binding.root)

        val buttonToDashboardOnSmartwatch = findViewById<ImageButton>(R.id.toDashboardButton)
        println(buttonToDashboardOnSmartwatch)
        buttonToDashboardOnSmartwatch.setOnClickListener {
            val intent = Intent(this, DashboardOnSmartwatch::class.java)
            startActivity(intent)
        }


        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
                when (result) {
                    true -> {
                        Log.i(TAG, "Body sensors permission granted")
                        // Only measure while the activity is at least in STARTED state.
                        // MeasureClient provides frequent updates, which requires increasing the
                        // sampling rate of device sensors, so we must be careful not to remain
                        // registered any longer than necessary.
                        lifecycleScope.launchWhenStarted {
                            viewModel.measureHeartRate()
                        }
                    }
                    false -> Log.i(TAG, "Body sensors permission not granted")
                }
            }

        // Bind viewmodel state to the UI.
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect {
                updateViewVisiblity(it)
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.heartRateAvailable.collect {
                binding.statusText.text = getString(R.string.measure_status, it)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.heartRateBpm.collect {
		binding.lastMeasuredValue.text = String.format("%.1f", it)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.heartRateBpmTime.collect {
                binding.timeNow.text = String.format(it.toString())
            }
        }
    }

    override fun onStart() {
        super.onStart()
        permissionLauncher.launch(android.Manifest.permission.BODY_SENSORS)
    }

    private fun uploadData() {
        binding!!.btnUploadData.setOnClickListener {
            // create a dummy data
            val hashMap = hashMapOf<String, Any>(
                "name" to "Mo",
                "city" to "Alexandria",
                "age" to 30
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
    private fun updateViewVisiblity(uiState: UiState) {
        (uiState is UiState.Startup).let {
            binding.progress.isVisible = it
        }
        // These views are visible when heart rate capability is not available.
        (uiState is UiState.HeartRateNotAvailable).let {
            binding.brokenHeart.isVisible = it
            binding.notAvailable.isVisible = it
        }
        // These views are visible when the capability is available.
        (uiState is UiState.HeartRateAvailable).let {
            binding.statusText.isVisible = it
            binding.lastMeasuredLabel.isVisible = it
            binding.lastMeasuredValue.isVisible = it
            binding.heart.isVisible = it
        }
    }
}
