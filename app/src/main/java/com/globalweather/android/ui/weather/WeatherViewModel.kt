package com.globalweather.android.ui.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.globalweather.android.logic.Repository
import com.globalweather.android.logic.model.Location

class WeatherViewModel:ViewModel() {

    private val locationLiveData = MutableLiveData<Location>()

    var locaionLng = ""
    var locationLat = ""
    var placeName = ""

    val weatherLiveData = Transformations.switchMap(locationLiveData) {location ->
        Repository.refreshWeather(location.lng, location.lat)
    }

    fun refreshWeather(lng: String, lat:String){
        locationLiveData.value = Location(lng,lat)
    }

}