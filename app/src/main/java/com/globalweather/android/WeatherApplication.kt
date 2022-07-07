package com.globalweather.android

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context


// create global context for applicaiton
class WeatherApplication : Application() {

    companion object{

        const val TOKEN = "Zhwtserelvi1b39p"   //token, weather API

        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

}

