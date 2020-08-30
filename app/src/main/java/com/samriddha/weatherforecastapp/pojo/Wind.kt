package com.samriddha.weatherforecastapp.pojo

import androidx.annotation.Keep

@Keep
data class Wind(
    val deg: Int,
    val speed: Double
)