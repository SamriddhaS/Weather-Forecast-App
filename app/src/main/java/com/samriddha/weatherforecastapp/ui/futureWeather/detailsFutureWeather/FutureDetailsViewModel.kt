package com.samriddha.weatherforecastapp.ui.futureWeather.detailsFutureWeather

import androidx.lifecycle.viewModelScope
import com.samriddha.weatherforecastapp.data.providers.UnitProvider
import com.samriddha.weatherforecastapp.data.repository.ForecastRepository
import com.samriddha.weatherforecastapp.ui.WeatherViewModel
import kotlinx.coroutines.async

class FutureDetailsViewModel(epochDate:Long,
                             private val forecastRepository: ForecastRepository,
                             unitProvider: UnitProvider
): WeatherViewModel(forecastRepository, unitProvider) {


    val detailFutureWeather = viewModelScope.async {
        forecastRepository.getDetailFutureWeather(epochDate)
    }
}