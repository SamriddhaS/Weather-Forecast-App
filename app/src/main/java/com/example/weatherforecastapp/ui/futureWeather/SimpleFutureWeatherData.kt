package com.example.weatherforecastapp.ui.futureWeather

import com.example.weatherforecastapp.pojo.Weather

interface SimpleFutureWeatherData {

    val date:Long
    val temp:Double
    val weatherDescription:List<Weather>

}