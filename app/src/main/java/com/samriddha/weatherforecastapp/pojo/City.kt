package com.samriddha.weatherforecastapp.pojo


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class City(
    @SerializedName("coord")
    val coordinates: Coord,
    val country: String,
    val id: Int,
    val name: String,
    val population: Int,
    val sunrise: Int,
    val sunset: Int,
    val timezone: Int
)