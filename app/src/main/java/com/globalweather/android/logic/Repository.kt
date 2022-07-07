package com.globalweather.android.logic

import android.util.Log
import androidx.lifecycle.liveData
import com.globalweather.android.logic.dao.PlaceDao
import com.globalweather.android.logic.model.Place
import com.globalweather.android.logic.model.Weather
import com.globalweather.android.logic.network.GlobalWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.CoroutineContext

//仓库层的主要工作就是判断调用方请求的数据应该是从本地数据库获取还是从网络数据源中获取。
//仓库层有点像是一个数据获取与缓存的中间层，在没有本地缓存数据的情况下就去网络层获取，如果本地已经没有缓存了，
//就直接将缓存数据返回。但是搜索城市数据的请求并没有太多缓存的必要，每次都发起网络请求去获取最新数据即可。
object Repository {


    /**
     * 旧版写法,太多try catch
     */
//    fun searchPlaces(query: String) = liveData(Dispatchers.IO) {  //Dispatchers.IO会使用一种较高并发线程策略
//        val result = try {
//            val placeResponse = GlobalWeatherNetwork.searchPlaces(query)
//            if (placeResponse.status == "ok") {
//                val places = placeResponse.places
//                Result.success(places)
//            } else {
//                Result.failure(RuntimeException("response status is${placeResponse.status}"))
//            }
//        } catch (e: Exception) {
//            Result.failure<List<Place>>(e)
//        }
//        emit(result)
//    }
//
//    fun refreshWeather(lng:String, lat:String) = liveData(Dispatchers.IO) {
//        var result = try {
//            //协程作用域
//            coroutineScope {
//                //分别在两个async函数中发起网络请求，并调用他们的awaits()方法，这样就可以保证
//                //两个网络请求都成功相应之后，才会进一步执行程序
//                val deferredRealtime = async {
//                    GlobalWeatherNetwork.getRealtimeWeather(lng, lat)
//                }
//                val deferredDaily = async {
//                    GlobalWeatherNetwork.getDailyWeather(lng, lat)
//                }
//                val realtimeResponse = deferredRealtime.await()
//                val dailyResponse = deferredDaily.await()
//                if(realtimeResponse.status == "ok" && dailyResponse.status == "ok"){
//                    val weather = Weather(realtimeResponse.result.realtime, dailyResponse.result.daily)
//                    Result.success(weather)
//                }else{
//                    Result.failure(
//                        RuntimeException(
//                            "realtime response status is ${realtimeResponse.status}" +
//                                    "daily response status is ${dailyResponse.status}"
//                        )
//                    )
//                }
//            }
//        }catch (e: Exception){
//            Result.failure<Weather>(e)
//        }
//        emit(result)
//    }


    /**
     * 新版写法，在某个统一的入口函数中进行封装，只要进行一次try catch处理即可
     */

    fun searchPlaces(query: String) = fire(Dispatchers.IO){
        val placeResponse = GlobalWeatherNetwork.searchPlaces(query)
        if(placeResponse.status == "ok"){
            val places = placeResponse.places
             Result.success(places)
        }else{
            Result.failure(RuntimeException("response status is ${placeResponse.status}"))
        }
    }

    fun refreshWeather(lng: String, lat: String, placeName: String) = fire(Dispatchers.IO) {
        coroutineScope {
            val deferredRealtime = async {
                GlobalWeatherNetwork.getRealtimeWeather(lng, lat)
            }
            val deferredDaily = async {
                GlobalWeatherNetwork.getDailyWeather(lng, lat)
            }
            val realtimeResponse = deferredRealtime.await()
            val dailyResponse = deferredDaily.await()
            if (realtimeResponse.status == "ok" && dailyResponse.status == "ok") {
                val weather = Weather(realtimeResponse.result.realtime, dailyResponse.result.daily)
                Result.success(weather)
            } else {
                Result.failure(
                    RuntimeException(
                        "realtime response status is ${realtimeResponse.status}" +
                                "daily response status is ${dailyResponse.status}"
                    )
                )
            }
        }
    }


    fun savePlace(place: Place) = PlaceDao.savePlace(place)

    fun getSavedPlace() = PlaceDao.getSavedPlace()

    fun isPlaceSaved() = PlaceDao.isPlaceSaved()


    //函数声明suspend关键字，表示所有传入的Lambda表达式中的代码也是拥有挂起函数上下文的
    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
        //在liveData()函数的代码块中统一进行了try catch处理
        liveData<Result<T>>(context) {
        val result = try {
            block()
        } catch (e: Exception) {
            Result.failure<T>(e)
        }
        emit(result)
    }
}