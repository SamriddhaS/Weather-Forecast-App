package com.example.weatherforecastapp.ui.futureWeather.detailsFutureWeather

import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapp.data.providers.UnitProvider
import com.example.weatherforecastapp.data.repository.ForecastRepository
import com.example.weatherforecastapp.ui.WeatherViewModel
import kotlinx.coroutines.async

class FutureDetailsViewModel(epochDate:Long,
                             private val forecastRepository: ForecastRepository,
                             unitProvider: UnitProvider
): WeatherViewModel(forecastRepository, unitProvider) {


    val detailFutureWeather = viewModelScope.async {
        forecastRepository.getDetailFutureWeather(epochDate)
    }
}