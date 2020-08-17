package com.samriddha.weatherforecastapp.data.local.entity
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

const val WEATHER_LOCATION_P_KEY = 0
@Entity(tableName = "weather_location")
data class WeatherLocation(
    val country: String,
    val name: String,
    val region: String
){
    @PrimaryKey(autoGenerate = false)
    var primaryKey:Int = WEATHER_LOCATION_P_KEY
    var timeOfFetching:Long = 0 //Field for saving time when data is fetched from api.
    var latitude:String = "0.00"
    var longitude:String = "0.00"
}