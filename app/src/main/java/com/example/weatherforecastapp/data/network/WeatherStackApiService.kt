package com.example.weatherforecastapp.data.network

import com.example.weatherforecastapp.pojo.CurrentWeatherResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val WEATHER_STACK_API_KEY = "9414e8821c0135afc3b7bd58d710a40c"

const val WEATHER_STACK_BASE_URL = "http://api.weatherstack.com/"

//http://api.weatherstack.com/current?access_key=9414e8821c0135afc3b7bd58d710a40c&query=Kolkata&units=f

interface WeatherStackApiService {

    @GET("current")
    suspend fun getCurrentWeather(@Query("query") location:String,
                                  @Query("units") unitSystem:String = "m"
    ):Response<CurrentWeatherResponse>

    companion object {

        operator fun invoke(
            connectivityInterceptor: ConnectivityInterceptor
        ): WeatherStackApiService {

            val requestIntercepter = Interceptor { chain ->
                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("access_key",
                        WEATHER_STACK_API_KEY
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
                .baseUrl(WEATHER_STACK_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeatherStackApiService::class.java)
        }
    }
}