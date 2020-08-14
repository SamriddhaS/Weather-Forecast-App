package com.samriddha.weatherforecastapp.data.providers

import com.samriddha.weatherforecastapp.data.local.entity.Request

interface UnitProvider {
    fun getUnit():String
    suspend fun hasUnitChanged(requestCurrentWeather: Request):Boolean
}