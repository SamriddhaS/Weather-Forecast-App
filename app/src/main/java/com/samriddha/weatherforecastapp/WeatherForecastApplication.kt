package com.samriddha.weatherforecastapp

import android.app.Application
import android.content.Context
import androidx.preference.PreferenceManager
import com.samriddha.weatherforecastapp.data.local.ForecastDatabase
import com.samriddha.weatherforecastapp.data.network.*
import com.samriddha.weatherforecastapp.data.providers.LocationProvider
import com.samriddha.weatherforecastapp.data.providers.LocationProviderImpl
import com.samriddha.weatherforecastapp.data.providers.UnitProvider
import com.samriddha.weatherforecastapp.data.providers.UnitProviderImpl
import com.samriddha.weatherforecastapp.data.repository.ForecastRepository
import com.samriddha.weatherforecastapp.data.repository.ForecastRepositoryImpl
import com.samriddha.weatherforecastapp.ui.currentWeather.CurrentWeatherViewModelFactory
import com.samriddha.weatherforecastapp.ui.futureWeather.FutureWeatherViewModelFactory
import com.samriddha.weatherforecastapp.ui.futureWeather.detailsFutureWeather.FutureDetailViewModelFactory
import com.google.android.gms.location.LocationServices
import com.jakewharton.threetenabp.AndroidThreeTen
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.*

class WeatherForecastApplication :Application(),KodeinAware {

    override val kodein = Kodein.lazy {

        import(androidXModule(this@WeatherForecastApplication))

        bind() from singleton { ForecastDatabase(instance()) }
        bind() from singleton { instance<ForecastDatabase>().currentWeatherDao() }
        bind() from singleton { instance<ForecastDatabase>().locationCurrentWeatherDao() }
        bind() from singleton { instance<ForecastDatabase>().futureWeatherDao() }
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind() from singleton { WeatherStackApiService(instance()) }
        bind() from singleton { OpenWeatherMapApiService(instance()) }
        bind<WeatherNetworkDataSource>() with singleton { WeatherNetworkDataSourceImpl(instance(),instance()) }
        bind() from provider { LocationServices.getFusedLocationProviderClient(instance<Context>()) }
        bind<LocationProvider>() with singleton { LocationProviderImpl(instance(),instance()) }
        bind<UnitProvider>() with singleton { UnitProviderImpl(instance()) }
        bind<ForecastRepository>() with singleton { ForecastRepositoryImpl(instance(),instance(),instance(),instance(),instance(),instance()) }
        bind() from provider { CurrentWeatherViewModelFactory(instance(),instance())}
        bind() from provider { FutureWeatherViewModelFactory(instance(),instance())}
        bind() from factory { epochDate:Long -> FutureDetailViewModelFactory(epochDate,instance(),instance())}

    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        PreferenceManager.setDefaultValues(this,R.xml.preferences,false)
    }


}