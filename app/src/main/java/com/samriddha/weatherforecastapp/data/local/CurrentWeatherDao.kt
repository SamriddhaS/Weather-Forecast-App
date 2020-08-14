package com.samriddha.weatherforecastapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.samriddha.weatherforecastapp.data.local.entity.CURRENT_WEATHER_BODY_P_KEY
import com.samriddha.weatherforecastapp.data.local.entity.CurrentWeather
import com.samriddha.weatherforecastapp.data.local.entity.PRIMARY_KEY_ID
import com.samriddha.weatherforecastapp.data.local.entity.Request

@Dao
interface CurrentWeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(currentWeather: CurrentWeather)

    @Query("select * from current_weather where id = $PRIMARY_KEY_ID")
    fun getCurrentWeatherResult():LiveData<CurrentWeather>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(request: Request)

    @Query("select unit from current_weather_unit where id = $CURRENT_WEATHER_BODY_P_KEY")
    fun getCurrentWeatherUnitDetails():String

    @Query("select * from current_weather_unit where id = $CURRENT_WEATHER_BODY_P_KEY")
    fun getRequestCurrentWeather(): Request

}