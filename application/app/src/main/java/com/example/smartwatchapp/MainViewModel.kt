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

import android.util.Log
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.DataTypeAvailability
import androidx.health.services.client.data.ExerciseConfig
import androidx.health.services.client.data.ExerciseType
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.sql.Time
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

//pour construire les vues

/**
 * Holds most of the interaction logic and UI state for the app.
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val healthServicesManager: HealthServicesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Startup)
    val uiState: StateFlow<UiState> = _uiState

    private val _heartRateAvailable = MutableStateFlow(DataTypeAvailability.UNKNOWN)
    val heartRateAvailable: StateFlow<DataTypeAvailability> = _heartRateAvailable

    private val _heartRateBpmStats = MutableStateFlow(0.0)
    val heartRateBpmStats: MutableStateFlow<Double> = _heartRateBpmStats

    private val _heartRateBpmTime = MutableStateFlow(DateFormat.DATE_FIELD)
    val heartRateBpmTime: MutableStateFlow<Int> = _heartRateBpmTime


    private val _heartRateBpm = MutableStateFlow(0.0)
    val heartRateBpm: StateFlow<Double> = _heartRateBpm

    init {

        // Check that the device has the heart rate capability and progress to the next state
        // accordingly.
        viewModelScope.launch {
            _uiState.value = if (healthServicesManager.hasHeartRateCapability()) {
                UiState.HeartRateAvailable
            } else {
                UiState.HeartRateNotAvailable
            }
        }
    }

    private fun upload(rate: Double, date: String, time: String, moment: String){
        val hashMap = hashMapOf<String, Any>(
            "rate" to rate,
            "date" to date,
            "time" to time,
            "moment" to moment
        )
        FirebaseUtils().fireStoreDatabase.collection("hearts")
            .add(hashMap)
            .addOnSuccessListener {
                Log.d(TAG, "Added heart document with ID ${it.id}")
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error adding heart document $exception")
            }
    }

    @ExperimentalCoroutinesApi
    suspend fun measureHeartRate() {
        var count = 0
        healthServicesManager.heartRateMeasureFlow().collect {
            when (it) {
                is MeasureMessage.MeasureAvailability -> {
                    Log.d(TAG, "Availability changed: ${it.availability}")
                    _heartRateAvailable.value = it.availability
                }
                is MeasureMessage.MeasureData -> {

                    var c : Calendar = Calendar.getInstance()
                    var df : SimpleDateFormat? = null
                    var formattedDate = ""
                    //var tf : SimpleDateFormat? = null
                    //var formattedTime = ""
                    df = SimpleDateFormat("dd-MM-yyyy HH:mm:ss a")
                    formattedDate = df!!.format(c.time)
                    var date = formattedDate.split(' ')[0]
                    var heure = formattedDate.split(' ')[1]
                    var moment = formattedDate.split(' ')[2]
                    val bpm = it.data.last().value
                    if(bpm!=0.0) {
                        count += 1
                    }
                    if((count%30 === 0) && (bpm != 0.0)) {
                        upload(bpm, date, heure, moment)
                    }


                    //tf = SimpleDateFormat("")
                    //formattedTime = tf!!.format(c.time)


                    Log.d(TAG, "Data update: $bpm")
                    Log.d(TAG, "Format date: $formattedDate")
                    //Log.d(TAG, "Format Time: $formattedTime")

                    _heartRateBpm.value = bpm

                }
                /*
                is MeasureMessage.MeasureDataStats -> {
                    val bpmstart = it.dataStats?.start
                    val bpmsEnd = it.dataStats?.end

                    Log.d(TAG, "Data update: $bpmstart")
                    _heartRateBpmStats.value = bpmstart.getLong(new TemportalField)
                }*/
                else -> {}
            }
        }
    }
}



sealed class UiState {
    object Startup : UiState()
    object HeartRateAvailable : UiState()
    object HeartRateNotAvailable : UiState()
}
