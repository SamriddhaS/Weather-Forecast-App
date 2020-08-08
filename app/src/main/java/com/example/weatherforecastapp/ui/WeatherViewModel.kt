package com.example.weatherforecastapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapp.data.providers.UnitProvider
import com.example.weatherforecastapp.data.repository.ForecastRepository
import com.example.weatherforecastapp.internal.UnitSystems
import kotlinx.coroutines.async

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
}