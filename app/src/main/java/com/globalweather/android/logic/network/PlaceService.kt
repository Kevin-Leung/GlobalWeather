package com.globalweather.android.logic.network

import retrofit2.Call
import com.globalweather.android.WeatherApplication
import com.globalweather.android.logic.model.PlaceResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceService {
    @GET("v2/place?token=${WeatherApplication.TOKEN}&lang=zh_CN")
    fun searchPlaces(@Query("query") query: String): Call<PlaceResponse>
}