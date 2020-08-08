package com.example.weatherforecastapp.data.providers

import com.example.weatherforecastapp.data.local.entity.WeatherLocation

interface LocationProvider {

    suspend fun hasLocationChanged(lastWeatherLocation: WeatherLocation):Boolean

    suspend fun getPreferredLocationString():String

    suspend fun getPreferredLocationStringLat():String

    suspend fun getPreferredLocationStringLong():String
}