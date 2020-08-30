package com.samriddha.weatherforecastapp.data.local.entity


import androidx.annotation.Keep
import androidx.room.*
import com.samriddha.weatherforecastapp.pojo.Main
import com.samriddha.weatherforecastapp.pojo.Weather
import com.samriddha.weatherforecastapp.pojo.Wind
import com.google.gson.annotations.SerializedName

@Keep
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