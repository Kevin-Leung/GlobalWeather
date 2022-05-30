package com.globalweather.android.ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.globalweather.android.logic.Repository
import com.globalweather.android.logic.model.Place


class PlaceViewModel: ViewModel() {

    private val searchLiveData = MutableLiveData<String>()

    //用于对界面上显示的城市数据进行缓存
    val placeList = ArrayList<Place>()

    val placeLiveData = Transformations.switchMap(searchLiveData){ query->
        Repository.searchPlaces(query)
    }

    fun searchPlaces(query:String){
        searchLiveData.value = query
    }
}