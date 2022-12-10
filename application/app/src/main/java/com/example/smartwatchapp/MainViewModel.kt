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
import androidx.health.services.client.data.DataTypeAvailability
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
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
    private val healthServicesManager: HealthServicesManager,
    private val repository: PassiveDataRepository
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
    val passiveDataEnabled: Flow<Boolean>
    val latestHeartRate = repository.latestHeartRate

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

        passiveDataEnabled = repository.passiveDataEnabled
            .distinctUntilChanged()
            .onEach { enabled ->
                viewModelScope.launch {
                    if (enabled)
                        healthServicesManager.registerForHeartRateData()
                    else
                        healthServicesManager.unregisterForHeartRateData()
                }
            }
    }

    // upload data in the "hearts" collection
    private fun uploadRate(rate: Double, date: String, time: String, moment: String, idSession: String){
        val hashMap = hashMapOf<String, Any>(
            "rate" to rate,
            "date" to date,
            "time" to time,
            "moment" to moment,
            "idSession" to idSession
        )
        FirebaseUtils().fireStoreDatabase.collection("hearts")
            .add(hashMap)

    }

    // upload data in the "stats" collection
    private fun uploadStat(avg: Double, min: Double, max: Double, idSession: String){

        val hashMap = hashMapOf<String, Any>(
            "idSession" to idSession,
            "avg" to avg,
            "min" to min,
            "max" to max
        )
        // use the add() method to create a document inside users collection
        FirebaseUtils().fireStoreDatabase.collection("stats")
            .add(hashMap)
            .addOnSuccessListener {
                Log.d(TAG, "Added stat document with ID ${it.id}")
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error adding stat document $exception")
            }
        fun togglePassiveData(enabled: Boolean) {
            viewModelScope.launch {
                repository.setPassiveDataEnabled(enabled)
            }
        }
    }


    @ExperimentalCoroutinesApi
    suspend fun measureHeartRate() {
        var count = 0
        var idSession = ""

        var rates = arrayOf<Double>().toMutableList()
        var datesStats = arrayOf<String>()
        var heuresStats = arrayOf<String>()
        var momentStats = arrayOf<String>()
        var avg = 0.0
        var min = 0.0
        var max = 0.0

        healthServicesManager.heartRateMeasureFlow().collect {
            when (it) {
                is MeasureMessage.MeasureAvailability -> {
                    Log.d(TAG, "Availability changed: ${it.availability}")
                    _heartRateAvailable.value = it.availability
                }
                is MeasureMessage.MeasureData -> {

                    var c: Calendar = Calendar.getInstance()
                    var df: SimpleDateFormat? = null
                    var formattedDate = ""

                    df = SimpleDateFormat("dd-MM-yyyy HH:mm:ss a")
                    formattedDate = df!!.format(c.time)
                    var date = formattedDate.split(' ')[0]
                    var heure = formattedDate.split(' ')[1]
                    var moment = formattedDate.split(' ')[2]

                    var avg = 0.0
                    var min = 0.0
                    var max = 0.0

                    val bpm = it.data.last().value
                    if (count == 0) {
                        idSession = date + heure + moment
                    }
                    if (bpm != 0.0) {
                        count += 1
                    }
                    if((count <= 60) && (count%5 === 0) && (bpm != 0.0)) {


                            uploadRate(bpm, date, heure, moment, idSession)

                            rates.add(bpm)

                            avg = rates.average()
                            min = rates.min()
                            max = rates.max()

                            if(count === 60){ // end of the session (after 1 min)
                            uploadStat(avg, min, max, idSession)
                            }

                    }



                    Log.d(TAG, "Data update: $bpm")
                    Log.d(TAG, "Format date: $formattedDate")

                    _heartRateBpm.value = bpm

                }

                else -> { }
            }
        }
    }
    fun togglePassiveData(enabled: Boolean) {
        viewModelScope.launch {
            repository.setPassiveDataEnabled(enabled)
        }
    }
}



sealed class UiState {
    object Startup : UiState()
    object HeartRateAvailable : UiState()
    object HeartRateNotAvailable : UiState()
}
