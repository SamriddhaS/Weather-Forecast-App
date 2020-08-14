package com.samriddha.weatherforecastapp.data.local.entity


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

const val PRIMARY_KEY_ID = 0

@Entity(tableName = "current_weather")
data class CurrentWeather(
    val feelslike: Double,

    val humidity: Double,

    @SerializedName("is_day")
    val isDay: String,

    val precip: Double,

    val pressure: Double,

    val temperature: Double,

    @SerializedName("weather_code")
    val weatherCode: Int,

    @SerializedName("weather_descriptions")
    val weatherDescriptions: List<String>,

    @SerializedName("weather_icons")
    val weatherIcons: List<String>,

    @SerializedName("wind_dir")
    val windDir: String,

    @SerializedName("wind_speed")
    val windSpeed: Double,

    val uv_index :Double,

    val cloudcover:Double,
    @SerializedName("visibility")
    val visibility:Double

){
    @PrimaryKey(autoGenerate = false)
    var id:Int = PRIMARY_KEY_ID
}