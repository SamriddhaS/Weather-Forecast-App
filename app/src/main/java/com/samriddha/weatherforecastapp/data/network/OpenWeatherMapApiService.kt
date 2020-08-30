package com.samriddha.weatherforecastapp.data.network

import com.samriddha.weatherforecastapp.BuildConfig
import com.samriddha.weatherforecastapp.pojo.FutureWeatherResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val OPEN_WEATHER_BASE_URL = "https://api.openweathermap.org/data/2.5/"

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
                        BuildConfig.OPEN_WEATHER_API_KEY /*use your own api key*/
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