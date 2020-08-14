package com.samriddha.weatherforecastapp.ui.currentWeather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.samriddha.weatherforecastapp.data.providers.UnitProvider
import com.samriddha.weatherforecastapp.data.repository.ForecastRepository

class CurrentWeatherViewModelFactory(
    private val forecastRepository:ForecastRepository,
    private val unitProvider: UnitProvider
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CurrentWeatherViewModel(forecastRepository,unitProvider) as T
    }
}