package com.example.weatherforecastapp.ui.futureWeather

import com.example.weatherforecastapp.R
import com.example.weatherforecastapp.data.providers.EpochTimeProvider
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.future_weather_item.*

class FutureWeatherItem(val simpleFutureWeatherData: SimpleFutureWeatherData, private val isMetric:Boolean) : Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.apply {
            val unit = if (isMetric) "°C" else "°F"
            tvFutureItemTemp.text = "${simpleFutureWeatherData.temp}$unit"
            tvFutureItemDescription.text = simpleFutureWeatherData.weatherDescription.first().description
            tvFutureItemDate.text =  EpochTimeProvider.epochToDate(simpleFutureWeatherData.date)
            tvFutureItemTime.text =  EpochTimeProvider.epochToTime(simpleFutureWeatherData.date)

        }
    }

    override fun getLayout() = R.layout.future_weather_item


}