package com.globalweather.android.logic

import androidx.lifecycle.liveData
import com.globalweather.android.logic.model.Place
import com.globalweather.android.logic.network.GlobalWeatherNetwork
import kotlinx.coroutines.Dispatchers

//仓库层的主要工作就是判断调用方请求的数据应该是从本地数据库获取还是从网络数据源中获取。
//仓库层有点像是一个数据获取与缓存的中间层，在没有本地缓存数据的情况下就去网络层获取，如果本地已经没有缓存了，
//就直接将缓存数据返回。但是搜索城市数据的请求并没有太多缓存的必要，每次都发起网络请求去获取最新数据即可。
object Repository {

    fun searchPlaces(query: String) = liveData(Dispatchers.IO) {  //Dispatchers.IO会使用一种较高并发线程策略
        val result = try {
            val placeResponse = GlobalWeatherNetwork.searchPlaces(query)
            if (placeResponse.status == "ok") {
                val places = placeResponse.places
                Result.success(places)
            } else {
                Result.failure(RuntimeException("response status is${placeResponse.status}"))
            }
        } catch (e: Exception) {
            Result.failure<List<Place>>(e)
        }
        emit(result)
    }

}