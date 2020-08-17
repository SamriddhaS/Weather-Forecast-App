package com.samriddha.weatherforecastapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.samriddha.weatherforecastapp.data.local.CurrentWeatherDao
import com.samriddha.weatherforecastapp.data.local.FutureWeatherDao
import com.samriddha.weatherforecastapp.data.local.LocationCurrentWeatherDao
import com.samriddha.weatherforecastapp.data.local.entity.CurrentWeather
import com.samriddha.weatherforecastapp.data.local.entity.WeatherLocation
import com.samriddha.weatherforecastapp.data.network.WeatherNetworkDataSource
import com.samriddha.weatherforecastapp.data.providers.LocationProvider
import com.samriddha.weatherforecastapp.data.providers.UnitProvider
import com.samriddha.weatherforecastapp.ui.futureWeather.SimpleFutureWeatherData
import com.samriddha.weatherforecastapp.data.providers.EpochTimeProvider
import com.samriddha.weatherforecastapp.ui.futureWeather.detailsFutureWeather.FutureDetailWeatherData
import com.samriddha.weatherforecastapp.pojo.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ForecastRepositoryImpl(
    private val currentWeatherDao: CurrentWeatherDao,
    private val locationCurrentWeatherDao: LocationCurrentWeatherDao,
    private val futureWeatherDao: FutureWeatherDao,
    private val locationProvider: LocationProvider,
    private val weatherNetworkDataSource: WeatherNetworkDataSource,
    private val unitProvider: UnitProvider
) : ForecastRepository {

    init {
        weatherNetworkDataSource.apply {

            downloadedCurrentWeather.observeForever { newCurrentWeather ->
                persistCurrentWeather(newCurrentWeather)
            }

            downloadedFutureWeather.observeForever{ newFutureWeather ->
                persistFutureWeather(newFutureWeather)
            }

        }
    }

    override suspend fun getCurrentWeather(): LiveData<CurrentWeather> {

        return withContext(Dispatchers.IO) {
            currentWeatherDao.getCurrentWeatherResult()
        }
    }

    override suspend fun getFutureWeatherHourlyList(todayDate:Long): LiveData<out List<SimpleFutureWeatherData>> {
        return withContext(Dispatchers.IO){
            return@withContext futureWeatherDao.getSimpleFutureWeatherList(todayDate)
        }
    }

    override suspend fun updateWeatherData(currentUnit: String) {
        initWeatherData(currentUnit)
    }

    override suspend fun getDetailFutureWeather(date: Long): LiveData<out FutureDetailWeatherData> {
        return withContext(Dispatchers.IO){
            return@withContext futureWeatherDao.getDetailsFutureWeather(date)
        }
    }

    override suspend fun getLocationCurrentWeather(): LiveData<WeatherLocation> {

        return withContext(Dispatchers.IO){
            return@withContext locationCurrentWeatherDao.getLocation()
        }
    }

    private suspend fun initWeatherData(currentUnit: String) {

        //Checking if the unit system setting is changed by matching with the unit system inside local database.
        val unit = currentWeatherDao.getRequestCurrentWeather()

        val lastWeatherLocation = locationCurrentWeatherDao.getLocationNonLive()

        //For updating weather database if app is loaded for first time or location is changed or unit is changed.
        if (lastWeatherLocation==null
            || locationProvider.hasLocationChanged(lastWeatherLocation)
            || unitProvider.hasUnitChanged(unit)){

            fetchCurrentWeather(currentUnit)
            fetchFutureWeather(currentUnit)
            return
        }

        //For updating weather in 30 min interval
        if (isFetchNeeded(lastWeatherLocation.timeOfFetching)) {
            Log.e("CallIsMade","Calling from Time passed")
            fetchCurrentWeather(currentUnit)
        }

        if (isFetchNeeded(lastWeatherLocation.timeOfFetching)){
            Log.e("CallIsMade","Calling from time passed")
            fetchFutureWeather(currentUnit)
        }

    }

    private suspend fun fetchCurrentWeather(currentUnit: String) {
        weatherNetworkDataSource.fetchCurrentWeather(locationProvider.getPreferredLocationString(), currentUnit)
    }

    private suspend fun fetchFutureWeather(currentUnit: String){

        val unit = if (currentUnit=="m") "metric" else "imperial"
        val lat = locationProvider.getPreferredLocationStringLat()
        val long = locationProvider.getPreferredLocationStringLong()

        //If lat long is same that means user is using a custom location to see weather and not device location
        if (lat==long)
            weatherNetworkDataSource.fetchFutureWeatherByName(lat,unit) // Fetching result by location name typed by user
        else
            weatherNetworkDataSource.fetchFutureWeatherByLatLon(lat,long,unit) // Fetching result by device location lat & long
    }

    private fun isFetchNeeded(lastFetchTime: Long): Boolean {

        //checking if the last fetched time is more than 30 min's
        val time = EpochTimeProvider.getTimeMinus(
            EpochTimeProvider.getCurrentEpoch(),5)

        return time >= lastFetchTime

    }

    private fun persistCurrentWeather(fetchedData: CurrentWeatherResponse) {

        GlobalScope.launch(Dispatchers.IO) {
            currentWeatherDao.upsert(fetchedData.current)
            currentWeatherDao.insert(fetchedData.request)

            val location = fetchedData.location
            location.timeOfFetching = EpochTimeProvider.getCurrentEpoch() // Recording time of fetching before saving to local db.
            location.latitude = locationProvider.getPreferredLocationStringLat()
            location.longitude = locationProvider.getPreferredLocationStringLong()
            locationCurrentWeatherDao.insert(location)

        }

    }

    private fun persistFutureWeather(futureWeather:FutureWeatherResponse) {

        fun deleteOldData(){
            val todayDate = EpochTimeProvider.getCurrentEpoch()
            futureWeatherDao.deleteOldData(todayDate)
        }

        GlobalScope.launch (Dispatchers.IO){
            val futureWeatherList = futureWeather.futureWeatherList
            futureWeatherDao.insert(futureWeatherList)
            deleteOldData()

        }
    }

}