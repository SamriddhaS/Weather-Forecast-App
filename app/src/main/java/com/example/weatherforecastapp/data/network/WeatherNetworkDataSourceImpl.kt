package com.example.weatherforecastapp.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherforecastapp.internal.NoInternetException
import com.example.weatherforecastapp.pojo.CurrentWeatherResponse
import com.example.weatherforecastapp.pojo.FutureWeatherResponse

class WeatherNetworkDataSourceImpl(
    private val currentWeatherStackApiService: WeatherStackApiService,
    private val futureWeatherApiService: OpenWeatherMapApiService
) : WeatherNetworkDataSource {

    //Current Weather
    private val downCurrentWeather = MutableLiveData<CurrentWeatherResponse>()
    override val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>
        get() = downCurrentWeather

    override suspend fun fetchCurrentWeather(location: String, unitSystem: String) {

        try {
            val fetchCurrentWeather = currentWeatherStackApiService.getCurrentWeather(location, unitSystem)

            if (fetchCurrentWeather.body()?.current!=null) {
                downCurrentWeather.postValue(fetchCurrentWeather.body())
            } else {
                Log.e(
                    "Connection",
                    "Location Not Available.${fetchCurrentWeather.message()}.${fetchCurrentWeather.errorBody()}"
                )
            }
        } catch (e: NoInternetException) {
            Log.e("Connection", "Internet Not Available", e)
        }
    }

    //Future Weather
    private val downFutureWeather = MutableLiveData<FutureWeatherResponse>()

    override val downloadedFutureWeather: LiveData<FutureWeatherResponse>
        get() = downFutureWeather

    override suspend fun fetchFutureWeatherByLatLon(lat: String, long: String, unitSystem: String) {

        try {
            val fetchedFutureWeather =
                futureWeatherApiService.getForecastWeatherByLatLon(lat, long, unitSystem)
            if (fetchedFutureWeather.raw().isSuccessful)
                downFutureWeather.postValue(fetchedFutureWeather.body())
            else
                Log.e(
                    "Connection",
                    "Location Not Available.${fetchedFutureWeather.message()}.${fetchedFutureWeather.errorBody()}"
                )

        } catch (e: NoInternetException) {
            Log.e("Connection", "Internet Not Available", e)
        }
    }

    override suspend fun fetchFutureWeatherByName(locationName: String, unitSystem: String) {

        try {
            val fetchedFutureWeather =
                futureWeatherApiService.getForecastWeatherByName(locationName, unitSystem)
            if (fetchedFutureWeather.isSuccessful)
                downFutureWeather.postValue(fetchedFutureWeather.body())
            else
                Log.e(
                    "Connection",
                    "Location Not Available.${fetchedFutureWeather.message()}.${fetchedFutureWeather.errorBody()}"
                )

        } catch (e: NoInternetException) {
            Log.e("Connection", "Internet Not Available", e)
        }
    }


}