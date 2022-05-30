package com.globalweather.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.globalweather.android.logic.network.PlaceService
import com.globalweather.android.logic.network.ServiceCreator

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
}