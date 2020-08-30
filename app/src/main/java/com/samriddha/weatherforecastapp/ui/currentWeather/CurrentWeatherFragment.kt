package com.samriddha.weatherforecastapp.ui.currentWeather

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.material.snackbar.Snackbar
import com.samriddha.weatherforecastapp.LifecycleBoundLocationManager
import com.samriddha.weatherforecastapp.R
import com.samriddha.weatherforecastapp.data.providers.EpochTimeProvider
import com.samriddha.weatherforecastapp.utils.*
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.coroutines.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

private const val REQUEST_CODE_LOCATION_PERMISSION = 1
class CurrentWeatherFragment() : Fragment(), KodeinAware,EasyPermissions.PermissionCallbacks {

    override val kodein by closestKodein()

    private val fusedLocationProviderClient: FusedLocationProviderClient by instance<FusedLocationProviderClient>()

    private val viewModelFactory by instance<CurrentWeatherViewModelFactory>()
    private lateinit var viewModel: CurrentWeatherViewModel
    private lateinit var snackbar: Snackbar

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult?) {
            super.onLocationResult(p0)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.current_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(CurrentWeatherViewModel::class.java)


        if (LocationUtility.hasLocationPermission(requireContext())){
            bindLocationManager()
            bindUi()
        }
        else
            requestPermissions()





    }

    private fun bindLocationManager() {

        LifecycleBoundLocationManager(
            this,
            fusedLocationProviderClient,
            locationCallback
        )

    }

    private fun initSnackBar(message:String,setAction:Boolean) {

        if (setAction){
            snackbar = Snackbar.make(snackCoordLayout,message,Snackbar.LENGTH_INDEFINITE)
            snackbar.setAction("Retry", View.OnClickListener {
                snackbar.dismiss()
                progressBar.visibility = View.VISIBLE
                bindUi()
            })
        }else{
            snackbar = Snackbar.make(snackCoordLayout,message,Snackbar.LENGTH_LONG)
        }

    }

    private fun bindUi() {

        viewLifecycleOwner.lifecycleScope.launch() {


            val job = async {
                try {

                    viewModel.updateWeatherData()

                }catch (e:NoInternetException){
                    if(progressBar.visibility==View.VISIBLE) progressBar.visibility = View.INVISIBLE
                    initSnackBar("Internet Not Available!!",true)
                    snackbar.show()
                }catch (e:ApiException){
                    if(progressBar.visibility==View.VISIBLE) progressBar.visibility = View.INVISIBLE
                    initSnackBar("Location Not Available,Try Different Location",false)
                    snackbar.show()
                }

            }

            job.await()

            val data = viewModel.currentWeather.await()
            data.observe(viewLifecycleOwner, Observer {

                if (it == null) return@Observer

                updateTemperature(it.temperature)
                updateDescription(it.weatherDescriptions.first())
                updateRealFeel(it.feelslike)
                updatePressure(it.pressure)
                updatePrecipitation(it.precip)
                updateHumidity(it.humidity)
                updateWind(it.windSpeed,it.windDir)
                updateUv(it.uv_index)
                updateCloudCover(it.cloudcover)
                updateVisibility(it.visibility)
            })

            val locationCurrentLocation = viewModel.locationWeather.await()
            locationCurrentLocation.observe(viewLifecycleOwner, Observer {

                if (it == null) return@Observer

                progressBar.visibility = View.GONE
                groupLoading.visibility = View.VISIBLE
                updateLocation(it.name,it.region,it.country)
                updateDate(EpochTimeProvider.getCurrentEpoch())
            })

        }
    }

    private fun requestPermissions(){

            EasyPermissions.requestPermissions(
                this,
                "Please Allow Above Permission",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION)

    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {

        if (EasyPermissions.somePermissionPermanentlyDenied(this,perms)){
            //If the user has denied the permissions permanently then this dialog will be shown and it will lead to the settings for changing permissions.
            AppSettingsDialog.Builder(this).build().show()
        } else{
            //If the permissions are not denied permanently then we will request the permissions again.
            requestPermissions()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        //If the user grants permission after rejecting it first time
        bindLocationManager()
        bindUi()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        /* "onRequestPermissionResult" is the default function that is called when we request any permission.We are using EasyPermission library
        * here that.So we are passing the flow of permission's by overriding this function and passing its parameters to
        * EasyPermissions.onRequestPermissionsResult(_,_,_,_) */
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this)
    }

    private fun updateLocation(location: String,region:String,country: String) {
        tvCurrentPlace.text = "$location,$region,$country"
    }

    private fun updateDate(localtimeEpoch: Long) {

        val date = EpochTimeProvider.epochToDate(localtimeEpoch)
        tvCurrentDate.text = date
    }

    private fun updateTemperature(tem: Double) {

        val unit = if (viewModel.isMetric) "°C" else "°F"
        tvCurrentTemp.text = "$tem$unit"
    }

    private fun updateDescription(condition: String) {

        tvCurrentDescription.text = condition
    }

    private fun updatePrecipitation(preci: Double) {

        val unit = if (viewModel.isMetric) "mm" else "in"
        tvCurrentPrep.text = "$preci$unit"
    }

    private fun updateWind(speed: Double, dir: String) {

        val unit = if (viewModel.isMetric) "kph" else "mph"
        tvCurrentWind.text = "$speed$unit | $dir"
    }

    private fun updatePressure(pressure: Double) {

        val unit = if (viewModel.isMetric) "mb" else "mb"
        tvCurrentPressure.text = "$pressure$unit"
    }

    private fun updateRealFeel(temp:Double){
        val unit = if (viewModel.isMetric) "°C" else "°F"
        tvCurrentRealFeel.text = "$temp$unit"
    }

    private fun updateHumidity(humidity:Double){

        tvCurrentHumidity.text = "$humidity g/cm"
    }

    private fun updateUv(uv:Double){
        tvCurrentUv.text = "$uv"
    }

    private fun updateCloudCover(cloud:Double){
        tvCurrentCloud.text = "$cloud Okta"
    }

    private fun updateVisibility(vis:Double){

        val unit = if (viewModel.isMetric) "km" else "m"
        tvCurrentVisibility.text = "$vis$unit"
    }

}