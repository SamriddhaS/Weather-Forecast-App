package com.samriddha.weatherforecastapp.ui.futureWeather

import com.samriddha.weatherforecastapp.pojo.Weather

interface SimpleFutureWeatherData {

    val date:Long
    val temp:Double
    val weatherDescription:List<Weather>

}