package com.samriddha.weatherforecastapp.data.providers

import com.samriddha.weatherforecastapp.data.local.entity.WeatherLocation

interface LocationProvider {

    suspend fun hasLocationChanged(lastWeatherLocation: WeatherLocation):Boolean

    suspend fun getPreferredLocationString():String

    suspend fun getPreferredLocationStringLat():String

    suspend fun getPreferredLocationStringLong():String
}