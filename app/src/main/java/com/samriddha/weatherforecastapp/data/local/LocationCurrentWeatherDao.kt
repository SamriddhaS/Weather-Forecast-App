package com.samriddha.weatherforecastapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.samriddha.weatherforecastapp.data.local.entity.WEATHER_LOCATION_P_KEY
import com.samriddha.weatherforecastapp.data.local.entity.WeatherLocation

@Dao
interface LocationCurrentWeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(weatherLocation: WeatherLocation)

    @Query("select * from weather_location where primaryKey = $WEATHER_LOCATION_P_KEY")
    fun getLocation():LiveData<WeatherLocation>

    @Query("select * from weather_location where primaryKey = $WEATHER_LOCATION_P_KEY")
    fun getLocationNonLive(): WeatherLocation?
}