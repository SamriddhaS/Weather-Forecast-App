package com.samriddha.weatherforecastapp.pojo


import com.samriddha.weatherforecastapp.data.local.entity.FutureWeatherHourlyList
import com.google.gson.annotations.SerializedName

data class FutureWeatherResponse(
    val city: City,
    val cod: String,
    @SerializedName("list")
    val futureWeatherList: List<FutureWeatherHourlyList>

)