package com.globalweather.android.logic.model

import com.google.gson.annotations.SerializedName


data class PlaceResponse(val status: String, val places: List<Place>)

data class Place(val name: String,
                 val location: Location,
                 //使用SerializedName，字体段名称会被替换成注解的名字
                 @SerializedName("formatted_address")
                 val address: String)

data class Location(val lng: String, val lat: String)