package com.samriddha.weatherforecastapp.data.providers

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.content.ContextCompat
import com.samriddha.weatherforecastapp.utils.LocationNotGrantedException
import com.samriddha.weatherforecastapp.utils.asDeferred
import com.samriddha.weatherforecastapp.data.local.entity.WeatherLocation
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.Deferred

const val USE_DEVICE_LOCATION_KEY = "USE_DEVICE_LOCATION"
const val USE_CUSTOM_LOCATION_KEY = "CHANGE_LOCATION"

class LocationProviderImpl(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    context: Context):PreferenceProvider(context), LocationProvider {

    private val appContext = context.applicationContext

    override suspend fun hasLocationChanged(lastWeatherLocation: WeatherLocation): Boolean {

        val deviceLocationChanged = try {
            hasDeviceLocationChanged(lastWeatherLocation)
        }catch (e:LocationNotGrantedException){
            return false
        }

        return deviceLocationChanged || hasCustomLocationChanged(lastWeatherLocation)
    }


    override suspend fun getPreferredLocationString(): String {

        if (isUsingDeviceLocation()){
            try {
                Log.e("Location:Try","Location:Try Pre")
                val deviceLocation = getLastDeviceLocation().await()
                    ?: return "${getCustomLocationName()}"

                Log.e("Location:Try","Location:Try Post")
                return "${deviceLocation.latitude},${deviceLocation.longitude}"
            }catch (e:LocationNotGrantedException){

                Log.e("Location:Catch","Location:Catch")
                return "${getCustomLocationName()}"
            }
        }else{
            Log.e("Location:Else","Location:Else")
            return "${getCustomLocationName()}"
        }

    }

    override suspend fun getPreferredLocationStringLat(): String {

        if (isUsingDeviceLocation()){
            try {
                val deviceLocation = getLastDeviceLocation().await()
                    ?: return "${getCustomLocationName()}"
                return "${deviceLocation.latitude}"
            }catch (e:LocationNotGrantedException){
                return "${getCustomLocationName()}"
            }
        }else
            return "${getCustomLocationName()}"
    }

    override suspend fun getPreferredLocationStringLong(): String {

        if (isUsingDeviceLocation()){
            try {
                val deviceLocation = getLastDeviceLocation().await()
                    ?: return "${getCustomLocationName()}"
                return "${deviceLocation.longitude}"
            }catch (e:LocationNotGrantedException){
                return "${getCustomLocationName()}"
            }
        }else
            return "${getCustomLocationName()}"
    }

    private suspend fun hasDeviceLocationChanged(location: WeatherLocation): Boolean {

        if (!isUsingDeviceLocation())
            return false


        if (location.latitude==location.longitude) //Changing from custom location to device location this condition will be true cuz
            return true                 // lat,long will contain name(ex:Kolkata) instead of coordinates.


        val deviceLocation = getLastDeviceLocation().await()
            ?:return false

        val comparisonThreshold = 0.03
        return Math.abs(deviceLocation.latitude - location.latitude.toDouble()) > comparisonThreshold &&
                Math.abs(deviceLocation.longitude - location.longitude.toDouble()) > comparisonThreshold

    }

    @SuppressLint("MissingPermission")
    private fun getLastDeviceLocation(): Deferred<Location?> {

        return if (hasLocationPermission())
            fusedLocationProviderClient.lastLocation.asDeferred()
        else
            throw LocationNotGrantedException()
    }

    private fun hasCustomLocationChanged(lastLocation: WeatherLocation):Boolean{

        return if (!isUsingDeviceLocation()){
            val customLocationName = getCustomLocationName()?.toLowerCase()
            customLocationName!=lastLocation.name.toLowerCase()

        }else false

    }

    private fun isUsingDeviceLocation(): Boolean {
        return preferences.getBoolean(USE_DEVICE_LOCATION_KEY,true)
    }

    private fun getCustomLocationName(): String? {
        return preferences.getString(USE_CUSTOM_LOCATION_KEY,null)
    }

    private fun hasLocationPermission():Boolean{

        return ContextCompat.checkSelfPermission(appContext,
        android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

    }


}