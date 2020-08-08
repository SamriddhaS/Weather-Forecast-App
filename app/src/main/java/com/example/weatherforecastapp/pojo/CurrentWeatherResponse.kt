package com.example.weatherforecastapp.pojo

import com.example.weatherforecastapp.data.local.entity.CurrentWeather
import com.example.weatherforecastapp.data.local.entity.Request
import com.example.weatherforecastapp.data.local.entity.WeatherLocation
import com.google.gson.annotations.SerializedName


data class CurrentWeatherResponse(
    @SerializedName("current")
    val current: CurrentWeather,
    val location: WeatherLocation,
    val request: Request
)