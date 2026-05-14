package com.example.weatherapp

import android.telecom.Call
import com.airbnb.lottie.model.content.TextRangeUnits
import retrofit2.http.GET
import retrofit2.http.Query

interface APIInterface {
    @GET("weather")
    fun getWeatherData(
        @Query("q") city: String,
        @Query("appid") appid: String,
        @Query("units") units: String
    ) : retrofit2.Call<WeatherApp>

}