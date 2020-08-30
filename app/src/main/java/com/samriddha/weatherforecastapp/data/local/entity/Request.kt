package com.samriddha.weatherforecastapp.data.local.entity

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

const val CURRENT_WEATHER_BODY_P_KEY = 0

@Keep
@Entity(tableName = "current_weather_unit")
data class Request(
    val language: String,
    val query: String,
    val type: String,
    val unit: String
){
    @PrimaryKey(autoGenerate = false)
    var id:Int =
        CURRENT_WEATHER_BODY_P_KEY
}