package com.example.weatherforecastapp.ui.futureWeather.detailsFutureWeather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecastapp.data.providers.UnitProvider
import com.example.weatherforecastapp.data.repository.ForecastRepository

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