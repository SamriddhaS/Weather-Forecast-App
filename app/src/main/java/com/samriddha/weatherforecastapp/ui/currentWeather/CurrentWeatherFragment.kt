package com.samriddha.weatherforecastapp.ui.currentWeather

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.samriddha.weatherforecastapp.R
import com.samriddha.weatherforecastapp.data.providers.EpochTimeProvider
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.coroutines.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class CurrentWeatherFragment() : Fragment(), KodeinAware {

    override val kodein by closestKodein()

    private val viewModelFactory by instance<CurrentWeatherViewModelFactory>()
    private lateinit var viewModel: CurrentWeatherViewModel

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

        bindUi()

    }


    private fun bindUi() {

        viewLifecycleOwner.lifecycleScope.launch() {

            val data = viewModel.currentWeather.await()
            val locationCurrentLocation = viewModel.locationWeather.await()

            locationCurrentLocation.observe(viewLifecycleOwner, Observer {

                if (it == null) return@Observer

                updateLocation(it.name,it.region,it.country)
                updateDate(it.localtimeEpoch)
            })

            data.observe(viewLifecycleOwner, Observer {

                if (it == null) return@Observer

                progressBar.visibility = View.GONE
                groupLoading.visibility = View.VISIBLE

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

        }
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