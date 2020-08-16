package com.samriddha.weatherforecastapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samriddha.weatherforecastapp.data.providers.UnitProvider
import com.samriddha.weatherforecastapp.data.repository.ForecastRepository
import com.samriddha.weatherforecastapp.utils.UnitSystems
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

abstract class WeatherViewModel(
    private val forecastRepository: ForecastRepository,
    unitProvider: UnitProvider
):ViewModel() {

    val unit = unitProvider.getUnit()

    val isMetric: Boolean
        get() = UnitSystems.m.name == unit

    val locationWeather = viewModelScope.async {
        forecastRepository.getLocationCurrentWeather()
    }

    suspend fun updateWeatherData(){
        withContext(Dispatchers.IO){
            forecastRepository.updateWeatherData(unit)
        }
    }

}