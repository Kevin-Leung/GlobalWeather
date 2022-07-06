package com.globalweather.android.ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.globalweather.android.logic.Repository
import com.globalweather.android.logic.model.Place


class PlaceViewModel: ViewModel() {

    private val searchLiveData = MutableLiveData<String>()

    //用于对界面上显示的城市数据进行缓存，这样就可以保证它们在手机屏幕发生旋转时不会发生丢失
    val placeList = ArrayList<Place>()

    val placeLiveData = Transformations.switchMap(searchLiveData){ query->
        Repository.searchPlaces(query)
    }

    //这里并没有直接调用仓库层的searchPlace()方法，而是将传入的搜索参数赋值给了一个searchLiveData对象，
    //并使用Transformations的switchMap()方法来观察这个对象，否则仓库仓返回的LiveData对象将无法进行观察
    fun searchPlaces(query:String){
        searchLiveData.value = query
    }

    fun savePlace(place:Place) = Repository.savePlace(place)
    fun getSavedPlace() = Repository.getSavedPlace()
    fun isPlaceSaved() = Repository.isPlaceSaved()
}