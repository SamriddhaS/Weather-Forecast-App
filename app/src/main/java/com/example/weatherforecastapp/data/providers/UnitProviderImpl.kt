package com.example.weatherforecastapp.data.providers

import android.content.Context
import com.example.weatherforecastapp.internal.UnitSystems
import com.example.weatherforecastapp.data.local.entity.Request

const val UNIT_SYSTEM = "UNIT_SYSTEM"

class UnitProviderImpl(context: Context) : PreferenceProvider(context), UnitProvider {

    override fun getUnit(): String {

        val unit = preferences.getString(UNIT_SYSTEM, UnitSystems.m.name)

        return unit!!
    }

    override suspend fun hasUnitChanged(requestCurrentWeather: Request): Boolean {

        val currentUnit = preferences.getString(UNIT_SYSTEM, UnitSystems.m.name)

        return currentUnit!=requestCurrentWeather.unit
    }
}