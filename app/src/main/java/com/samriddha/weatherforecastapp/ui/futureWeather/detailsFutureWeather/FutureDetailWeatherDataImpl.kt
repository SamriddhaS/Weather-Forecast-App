package com.samriddha.weatherforecastapp.ui.futureWeather.detailsFutureWeather

import androidx.room.ColumnInfo
import com.samriddha.weatherforecastapp.pojo.Weather

class FutureDetailWeatherDataImpl(

    @ColumnInfo(name = "date")
    override val dateEpoch: Long,

    @ColumnInfo(name = "weatherInfo_temp")
    override val temp: Double,

    @ColumnInfo(name = "weatherInfo_feelsLike")
    override val feelsLike: Double,

    @ColumnInfo(name = "weatherDescription")
    override val weatherDescription: List<Weather>,

    @ColumnInfo(name = "weatherInfo_humidity")
    override val humidity: Double,

    @ColumnInfo(name = "weatherInfo_pressure")
    override val pressure: Double,

    @ColumnInfo(name = "wind_speed")
    override val windSpeed: Double,

    @ColumnInfo(name = "weatherInfo_tempMax")
    override val tempMax: Double,

    @ColumnInfo(name = "weatherInfo_tempMin")
    override val tempMin: Double
) : FutureDetailWeatherData {

}