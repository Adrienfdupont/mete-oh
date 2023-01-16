package com.example.mete_oh
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*

interface WeatherService{

    @GET("?q=lille&units=metric&appid=7d1c3e5d288d7b14cbe8db0ec6e57e11")
    fun getWeather(): Call<JsonObject>
}