package com.example.weatherforecastapp.data.local.entity


import androidx.room.*
import com.example.weatherforecastapp.pojo.Main
import com.example.weatherforecastapp.pojo.Weather
import com.example.weatherforecastapp.pojo.Wind
import com.google.gson.annotations.SerializedName


@Entity(tableName = "future_weather_hourly_list",indices = [Index(value = ["date"],unique = true)])

data class FutureWeatherHourlyList(

    @SerializedName("dt")
    val date: Long,

    @SerializedName("dt_txt")
    val dateText: String,

    @Embedded(prefix = "weatherInfo_")
    @SerializedName("main")
    val weatherInfo: Main,


    @SerializedName("weather")
    val weatherDescription: List<Weather>,

    @Embedded(prefix = "wind_")
    val wind: Wind,

    @PrimaryKey(autoGenerate = true)
    val primaryKey: Int? = null
)