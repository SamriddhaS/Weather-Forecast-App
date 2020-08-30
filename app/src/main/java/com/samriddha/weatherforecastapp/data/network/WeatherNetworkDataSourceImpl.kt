package com.samriddha.weatherforecastapp.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.samriddha.weatherforecastapp.utils.NoInternetException
import com.samriddha.weatherforecastapp.pojo.CurrentWeatherResponse
import com.samriddha.weatherforecastapp.pojo.FutureWeatherResponse
import com.samriddha.weatherforecastapp.utils.ApiException

class WeatherNetworkDataSourceImpl(
    private val currentWeatherStackApiService: WeatherStackApiService,
    private val futureWeatherApiService: OpenWeatherMapApiService
) : WeatherNetworkDataSource {

    //Current Weather
    private val downCurrentWeather = MutableLiveData<CurrentWeatherResponse>()
    override val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>
        get() = downCurrentWeather

    override suspend fun fetchCurrentWeather(location: String, unitSystem: String) {

            val fetchCurrentWeather = currentWeatherStackApiService.getCurrentWeather(location, unitSystem)

            if (fetchCurrentWeather.body()?.current!=null) {
                downCurrentWeather.postValue(fetchCurrentWeather.body())
            } else {

                Log.e(
                    "Connection",
                    "Location Not Available.${fetchCurrentWeather.code()}.${fetchCurrentWeather.errorBody()}"
                )
                throw ApiException()

            }

    }

    //Future Weather
    private val downFutureWeather = MutableLiveData<FutureWeatherResponse>()

    override val downloadedFutureWeather: LiveData<FutureWeatherResponse>
        get() = downFutureWeather

    override suspend fun fetchFutureWeatherByLatLon(lat: String, long: String, unitSystem: String) {

            val fetchedFutureWeather =
                futureWeatherApiService.getForecastWeatherByLatLon(lat, long, unitSystem)
            if (fetchedFutureWeather.raw().isSuccessful)
                downFutureWeather.postValue(fetchedFutureWeather.body())
            else{
                Log.e("Connection","Location Not Available.${fetchedFutureWeather.message()}.${fetchedFutureWeather.errorBody()}")
                throw ApiException()
            }

    }

    override suspend fun fetchFutureWeatherByName(locationName: String, unitSystem: String) {

            val fetchedFutureWeather =
                futureWeatherApiService.getForecastWeatherByName(locationName, unitSystem)
            if (fetchedFutureWeather.isSuccessful)
                downFutureWeather.postValue(fetchedFutureWeather.body())
            else{
                Log.e("Connection","Location Not Available.${fetchedFutureWeather.message()}.${fetchedFutureWeather.errorBody()}")
                throw ApiException()
            }

    }


}