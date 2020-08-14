package com.samriddha.weatherforecastapp.ui.futureWeather.detailsFutureWeather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.samriddha.weatherforecastapp.data.providers.UnitProvider
import com.samriddha.weatherforecastapp.data.repository.ForecastRepository

class FutureDetailViewModelFactory(
    private val epochDate:Long,
    private val forecastRepository: ForecastRepository,
    private val unitProvider: UnitProvider
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FutureDetailsViewModel(epochDate, forecastRepository, unitProvider) as T
    }

}