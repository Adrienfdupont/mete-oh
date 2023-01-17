package com.example.mete_oh

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // création d'une instance retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/weather/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val weatherService = retrofit.create(WeatherService::class.java)

        // appel de l'API
        val result = weatherService.getWeather()

        result.enqueue(object: Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful){
                    val result = response.body()

                    // afficher la ville
                    val city = result?.get("name")?.asString
                    val tvCity: TextView = findViewById(R.id.tvCity)
                    tvCity.text = city

                    // afficher la temperature
                    val temperature = result?.get("main")?.asJsonObject?.get("temp")?.asString
                    val tvTemperature: TextView = findViewById(R.id.tvTemperature)
                    tvTemperature.text = "$temperature °C"

                    // afficher la temperature
                    val wind = result?.get("wind")?.asJsonObject?.get("speed")?.asString
                    val tvWind: TextView = findViewById(R.id.tvWind)
                    tvWind.text = "$wind km/h"

                    // afficher l'image
                    val imageView: ImageView = findViewById(R.id.image)
                    val weather = result?.get("weather")?.asJsonArray
                    val icon = weather?.get(0)?.asJsonObject?.get("icon")?.asString
                    Picasso.get().load("https://openweathermap.org/img/w/$icon.png").into(imageView)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(applicationContext, "Erreur serveur", Toast.LENGTH_SHORT).show()
            }

        })
    }
}