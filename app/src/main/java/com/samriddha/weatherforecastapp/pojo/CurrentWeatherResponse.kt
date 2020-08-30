package com.samriddha.weatherforecastapp.pojo

import androidx.annotation.Keep
import com.samriddha.weatherforecastapp.data.local.entity.CurrentWeather
import com.samriddha.weatherforecastapp.data.local.entity.Request
import com.samriddha.weatherforecastapp.data.local.entity.WeatherLocation
import com.google.gson.annotations.SerializedName

@Keep
data class CurrentWeatherResponse(
    @SerializedName("current")
    val current: CurrentWeather,
    val location: WeatherLocation,
    val request: Request
)