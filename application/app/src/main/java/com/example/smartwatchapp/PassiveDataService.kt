package com.example.smartwatchapp

import android.os.SystemClock
import android.util.Log
import androidx.health.services.client.PassiveListenerService
import androidx.health.services.client.data.*
import dagger.Provides
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking
import java.sql.Time
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class PassiveDataService : PassiveListenerService() {
    @Inject
    lateinit var repository: PassiveDataRepository

    var rates = arrayOf<Double>().toMutableList()
    var avg = 0.0
    var min = 0.0
    var max = 0.0
    private val _heartRateBpm = MutableStateFlow(0.0)

    override fun onNewDataPointsReceived(dataPoints: DataPointContainer) {
        runBlocking {
            dataPoints.getData(DataType.HEART_RATE_BPM).latestHeartRate()?.let {
                when(it){
                    (it) ->{
                        repository.storeLatestHeartRate(it)
                var c: Calendar = Calendar.getInstance()
                var df: SimpleDateFormat? = null

                        var formattedDate = ""
                df = SimpleDateFormat("dd-MM-yyyy HH:mm:ss a")
                formattedDate = df!!.format(c.time)
                var date = formattedDate.split(' ')[0]
                var heure = formattedDate.split(' ')[1]
                var moment = formattedDate.split(' ')[2]
                val bpm = it

                    uploadRate(bpm, date, heure, moment)
                    rates.add(bpm)
                    avg = rates.average()
                    min = rates.min()
                    max = rates.max()

                        uploadStat(avg, min, max, date, heure)

                Log.d(TAG, "Data update: $bpm")
                Log.d(TAG, "Format date: $formattedDate")

                _heartRateBpm.value = bpm
            }
        }
            }
        }
        }

    // upload data in the "stats" collection
    private fun uploadStat(avg: Double, min: Double, max: Double, date: String, heure: String){
        val hashMap = hashMapOf<String, Any>(
            "avg" to avg,
            "min" to min,
            "max" to max,
            "date" to date,
            "heure" to heure
        )
        // use the add() method to create a document inside users collection
        FirebaseUtils().fireStoreDatabase.collection("stats")
            .add(hashMap)
    }
    
    // upload data in the "hearts" collection
    private fun uploadRate(rate: Double, date: String, time: String, moment: String){
        val hashMap = hashMapOf<String, Any>(
            "rate" to rate,
            "date" to date,
            "time" to time,
            "moment" to moment,
        )
        FirebaseUtils().fireStoreDatabase.collection("hearts")
            .add(hashMap)
    }
}
