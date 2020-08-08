package com.example.weatherforecastapp.data.network

import com.example.weatherforecastapp.pojo.FutureWeatherResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val OPEN_WEATHER_API_KEY = "a4680def7e63766deb0ce3db92a76191"

const val OPEN_WEATHER_BASE_URL = "https://api.openweathermap.org/data/2.5/"

//https://api.openweathermap.org/data/2.5/forecast?lat=22.5697&lon=88.3697&appid=a4680def7e63766deb0ce3db92a76191&units=metric
//https://api.openweathermap.org/data/2.5/forecast?q=delhi&appid=a4680def7e63766deb0ce3db92a76191&units=metric

interface OpenWeatherMapApiService {

    @GET("forecast")
    suspend fun getForecastWeatherByLatLon(@Query("lat") lat:String,
                                           @Query("lon") lon:String,
                                           @Query("units") unit:String)
    :Response<FutureWeatherResponse>

    @GET("forecast")
    suspend fun getForecastWeatherByName(@Query("q") locationName:String,
                                           @Query("units") unit:String)
            :Response<FutureWeatherResponse>

    companion object {
        operator fun invoke(
            connectivityInterceptor: ConnectivityInterceptor
        ): OpenWeatherMapApiService {

            val requestIntercepter = Interceptor { chain ->
                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("appid",
                        OPEN_WEATHER_API_KEY
                    )
                    .build()

                val mainUrl = chain.request().newBuilder().url(url).build()

                return@Interceptor chain.proceed(mainUrl)
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestIntercepter)
                .addInterceptor(connectivityInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(OPEN_WEATHER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OpenWeatherMapApiService::class.java)
        }
    }


}