package com.example.weatherforecastapp.ui.futureWeather.detailsFutureWeather

import com.example.weatherforecastapp.pojo.Weather

interface FutureDetailWeatherData {

    val dateEpoch:Long
    val temp:Double
    val feelsLike:Double
    val weatherDescription:List<Weather>
    val humidity:Double
    val pressure:Double
    val windSpeed:Double
    val tempMax:Double
    val tempMin:Double

}