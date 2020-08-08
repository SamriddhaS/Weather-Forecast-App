package com.example.weatherforecastapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforecastapp.ui.futureWeather.SimpleFutureWeatherDataImpl
import com.example.weatherforecastapp.ui.futureWeather.detailsFutureWeather.FutureDetailWeatherDataImpl
import com.example.weatherforecastapp.data.local.entity.FutureWeatherHourlyList


@Dao
interface FutureWeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(futureWeatherHourlyList: List<FutureWeatherHourlyList>)

    @Query("select * from  future_weather_hourly_list where date >= :todayDate")
    fun getSimpleFutureWeatherList(todayDate:Long):LiveData<List<SimpleFutureWeatherDataImpl>>

    @Query("delete from future_weather_hourly_list where date < :todayDate")
    fun deleteOldData(todayDate: Long)

    @Query("select * from future_weather_hourly_list where date = :date")
    fun getDetailsFutureWeather(date:Long):LiveData<FutureDetailWeatherDataImpl>

}