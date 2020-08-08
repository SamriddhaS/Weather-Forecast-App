package com.example.weatherforecastapp.data.providers

import com.example.weatherforecastapp.data.local.entity.Request

interface UnitProvider {
    fun getUnit():String
    suspend fun hasUnitChanged(requestCurrentWeather: Request):Boolean
}