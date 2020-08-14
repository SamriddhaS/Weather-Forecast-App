package com.samriddha.weatherforecastapp.ui.futureWeather.detailsFutureWeather

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
import com.samriddha.weatherforecastapp.data.providers.UnitProvider
import com.samriddha.weatherforecastapp.data.repository.ForecastRepository
import com.samriddha.weatherforecastapp.internal.EpochDateNotFoundException
import kotlinx.android.synthetic.main.future_details_fragment.tvDetailDate
import kotlinx.android.synthetic.main.future_details_fragment.tvDetailsDescription
import kotlinx.android.synthetic.main.future_details_fragment.tvDetailsHumidity
import kotlinx.android.synthetic.main.future_details_fragment.tvDetailsPlace
import kotlinx.android.synthetic.main.future_details_fragment.tvDetailsPressure
import kotlinx.android.synthetic.main.future_details_fragment.tvDetailsRealFeel
import kotlinx.android.synthetic.main.future_details_fragment.tvDetailsTemp
import kotlinx.android.synthetic.main.future_details_fragment.tvDetailsTempMin
import kotlinx.android.synthetic.main.future_details_fragment.tvDetailsWind
import kotlinx.android.synthetic.main.future_details_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.*

const val EPOCH_DATE_PASS_KEY = "EPOCH_TIME_STAMP"
class FutureDetailsFragment : Fragment(),KodeinAware {

    override val kodein by closestKodein()

    private lateinit var viewModel: FutureDetailsViewModel

    private val repository by instance<ForecastRepository>()
    private val unitProvider by instance<UnitProvider>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.future_details_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val date = arguments?.getLong(EPOCH_DATE_PASS_KEY) ?: throw EpochDateNotFoundException()

        val viewModelFactoryInstanceFactory = FutureDetailViewModelFactory(date,repository,unitProvider)
        viewModel = ViewModelProvider(this,viewModelFactoryInstanceFactory).get(FutureDetailsViewModel::class.java)

        bindUi()

    }

    private fun bindUi() {

        viewLifecycleOwner.lifecycleScope.launch {

            val futureWeatherData = viewModel.detailFutureWeather.await()
            val locationData = viewModel.locationWeather.await()

            locationData.observe(viewLifecycleOwner, Observer {

                if (it==null) return@Observer
                updateLocation(it.name,it.region,it.country)

            })

            futureWeatherData.observe(viewLifecycleOwner, Observer {

                if (it==null) return@Observer

                progressBar2.visibility = View.GONE
                detailWeatherGroupLoading.visibility = View.VISIBLE

                updateDate(it.dateEpoch)
                updateTime(it.dateEpoch)
                updateTemperature(it.temp)
                updateDescription("${it.weatherDescription.first().main},${it.weatherDescription.first().description}")
                updateRealFeel(it.feelsLike)
                updateTempMax(it.tempMax)
                updateTempMin(it.tempMin)
                updateWind(it.windSpeed)
                updatePressure(it.pressure)
                updateHumidity(it.humidity)

            })

        }
    }

    private fun updateLocation(location: String,region:String,country: String) {
        tvDetailsPlace.text = "$location,$region,$country"
    }

    private fun updateDate(localtimeEpoch: Long) {

        val date = EpochTimeProvider.epochToDate(localtimeEpoch)
        tvDetailDate.text = date
    }
    private fun updateTime(localtimeEpoch: Long) {

        val time = EpochTimeProvider.epochToTime(localtimeEpoch)
        tvDetailsTime.text = time
    }

    private fun updateTemperature(tem: Double) {

        val unit = if (viewModel.isMetric) "°C" else "°F"
        tvDetailsTemp.text = "$tem$unit"
    }

    private fun updateDescription(condition: String) {

        tvDetailsDescription.text = condition

    }

    private fun updateWind(speed: Double) {

        val unit = if (viewModel.isMetric) "kph" else "mph"
        tvDetailsWind.text = "$speed$unit"
    }

    private fun updatePressure(pressure: Double) {

        val unit = if (viewModel.isMetric) "mb" else "mb"
        tvDetailsPressure.text = "$pressure$unit"
    }

    private fun updateRealFeel(temp:Double){
        val unit = if (viewModel.isMetric) "°C" else "°F"
        tvDetailsRealFeel.text = "$temp$unit"
    }

    private fun updateHumidity(humidity:Double){

        tvDetailsHumidity.text = "$humidity g/cm"
    }

    private fun updateTempMin(temp:Double){

        val unit = if (viewModel.isMetric) "°C" else "°F"
        tvDetailsTempMin.text = "$temp$unit"

    }
    private fun updateTempMax(temp:Double){

        val unit = if (viewModel.isMetric) "°C" else "°F"
        tvDetailTempMax.text = "$temp$unit"
    }


}