package com.samriddha.weatherforecastapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.samriddha.weatherforecastapp.data.local.entity.CurrentWeather
import com.samriddha.weatherforecastapp.data.local.entity.FutureWeatherHourlyList
import com.samriddha.weatherforecastapp.data.local.entity.Request
import com.samriddha.weatherforecastapp.data.local.entity.WeatherLocation

@TypeConverters(
    CurrentWeatherTypeConverter::class,
    ObjectListTypeConverter::class
)
@Database(
    entities = [CurrentWeather::class,
        Request::class,
        WeatherLocation::class,
        FutureWeatherHourlyList::class],
    version = 1
)
abstract class ForecastDatabase : RoomDatabase() {

    abstract fun currentWeatherDao(): CurrentWeatherDao
    abstract fun locationCurrentWeatherDao(): LocationCurrentWeatherDao
    abstract fun futureWeatherDao(): FutureWeatherDao

    companion object {
        @Volatile
        private var instance: ForecastDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ForecastDatabase::class.java,
                "forecast.db"
            ).build()

    }

}
