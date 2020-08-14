package com.samriddha.weatherforecastapp.data.repository

import androidx.lifecycle.LiveData
import com.samriddha.weatherforecastapp.ui.futureWeather.SimpleFutureWeatherData
import com.samriddha.weatherforecastapp.ui.futureWeather.detailsFutureWeather.FutureDetailWeatherData
import com.samriddha.weatherforecastapp.data.local.entity.CurrentWeather
import com.samriddha.weatherforecastapp.data.local.entity.WeatherLocation

interface ForecastRepository {

    suspend fun getCurrentWeather(currentUnit:String):LiveData<CurrentWeather>

    suspend fun getLocationCurrentWeather():LiveData<WeatherLocation>

    suspend fun getFutureWeatherHourlyList(todayDate:Long,currentUnit: String):LiveData<out List<SimpleFutureWeatherData>>

    suspend fun getDetailFutureWeather(date:Long):LiveData<out FutureDetailWeatherData>

}