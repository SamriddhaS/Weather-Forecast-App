package com.samriddha.weatherforecastapp.ui.futureWeather

import androidx.lifecycle.viewModelScope
import com.samriddha.weatherforecastapp.data.providers.UnitProvider
import com.samriddha.weatherforecastapp.data.repository.ForecastRepository
import com.samriddha.weatherforecastapp.ui.WeatherViewModel
import com.samriddha.weatherforecastapp.data.providers.EpochTimeProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class FutureWeatherViewModel(
    private val forecastRepository: ForecastRepository,
    unitProvider: UnitProvider
) :
    WeatherViewModel(forecastRepository, unitProvider) {

    val futureWeather = viewModelScope.async {

        val todayDate = EpochTimeProvider.getCurrentEpoch()
        forecastRepository.getFutureWeatherHourlyList(todayDate)
    }

}