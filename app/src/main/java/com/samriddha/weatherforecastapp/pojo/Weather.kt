package com.samriddha.weatherforecastapp.pojo

import androidx.annotation.Keep

@Keep
data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)