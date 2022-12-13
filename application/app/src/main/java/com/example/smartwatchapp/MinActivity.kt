package com.example.smartwatchapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MinActivity : MyActivity() {

    override var id = R.id.textViewMin
    override var stat: String = "min"
    override var idComment = R.id.commentMin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_min)
        getData()
    }
}