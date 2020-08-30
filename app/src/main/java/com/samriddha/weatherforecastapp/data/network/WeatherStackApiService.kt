package com.samriddha.weatherforecastapp.data.network

import com.samriddha.weatherforecastapp.BuildConfig
import com.samriddha.weatherforecastapp.pojo.CurrentWeatherResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val WEATHER_STACK_BASE_URL = "http://api.weatherstack.com/"


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
                        BuildConfig.WEATHER_STACK_API_KEY /*use your own api key*/
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