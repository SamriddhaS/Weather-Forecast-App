package com.samriddha.weatherforecastapp.data.network

import androidx.lifecycle.LiveData
import com.samriddha.weatherforecastapp.pojo.CurrentWeatherResponse
import com.samriddha.weatherforecastapp.pojo.FutureWeatherResponse

interface WeatherNetworkDataSource {

    val downloadedCurrentWeather :LiveData<CurrentWeatherResponse>
    val downloadedFutureWeather :LiveData<FutureWeatherResponse>

    suspend fun fetchCurrentWeather(location:String, unitSystem:String = "m")

    suspend fun fetchFutureWeatherByLatLon(lat:String, long:String, unitSystem: String= "metric")

    suspend fun fetchFutureWeatherByName(locationName:String,unitSystem: String= "metric")
}