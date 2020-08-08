package com.example.weatherforecastapp.ui.currentWeather

import androidx.lifecycle.*
import com.example.weatherforecastapp.data.providers.UnitProvider
import com.example.weatherforecastapp.data.repository.ForecastRepository
import com.example.weatherforecastapp.ui.WeatherViewModel
import kotlinx.coroutines.async

class CurrentWeatherViewModel(
    private val forecastRepository: ForecastRepository,
    unitProvider: UnitProvider
) : WeatherViewModel(forecastRepository, unitProvider) {


    val currentWeather = viewModelScope.async {
        forecastRepository.getCurrentWeather(unit)
    }

}





