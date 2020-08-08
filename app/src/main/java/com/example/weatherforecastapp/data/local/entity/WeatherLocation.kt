package com.example.weatherforecastapp.data.local.entity
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

const val WEATHER_LOCATION_P_KEY = 0
@Entity(tableName = "weather_location")
data class WeatherLocation(
    val country: String,
    val lat: String,
    val localtime: String,
    @SerializedName("localtime_epoch")
    val localtimeEpoch: Long,
    val lon: String,
    val name: String,
    val region: String,
    @SerializedName("timezone_id")
    val timezoneId: String,
    @SerializedName("utc_offset")
    val utcOffset: String
){
    @PrimaryKey(autoGenerate = false)
    var primaryKey:Int =
        WEATHER_LOCATION_P_KEY

    var timeOfFetching:Long = 0 //Field for saving time when data is fetched from api.
}