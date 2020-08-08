package com.example.weatherforecastapp.ui.futureWeather

import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapp.data.providers.UnitProvider
import com.example.weatherforecastapp.data.repository.ForecastRepository
import com.example.weatherforecastapp.ui.WeatherViewModel
import com.example.weatherforecastapp.data.providers.EpochTimeProvider
import kotlinx.coroutines.async

class FutureWeatherViewModel(
    private val forecastRepository: ForecastRepository,
    unitProvider: UnitProvider
) :
    WeatherViewModel(forecastRepository, unitProvider) {

    val futureWeather = viewModelScope.async {

        val todayDate = EpochTimeProvider.getCurrentEpoch()
        forecastRepository.getFutureWeatherHourlyList(todayDate,unit)
    }

}