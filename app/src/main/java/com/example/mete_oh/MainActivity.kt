package com.example.mete_oh

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    // déclarer la BottomNavigationView
    lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // déclarer la bottomNav
        bottomNav = findViewById(R.id.bottomNav)

        // indiquer l'icône à sélectionner dans la bottomNav
        bottomNav.menu.findItem(R.id.home).isChecked = true

        // changer d'Activity lors d'un clic sur une icône de la bottomNav
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }

                R.id.favorites -> {
                    val intent = Intent(this, SecondActivity::class.java)
                    startActivity(intent)
                }
            }

            // nécessaire pour l'emploi du "when"
            return@setOnItemSelectedListener true
        }

        // récupérer les éléments du layout
        val tvCity: TextView = findViewById(R.id.tvCity)
        val tvTemperature: TextView = findViewById(R.id.tvTemperature)
        val tvWind: TextView = findViewById(R.id.tvWind)
        val imageView: ImageView = findViewById(R.id.image)

        // récupérer les data
        val sharedPref = this?.getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE
        ) ?: return
        val dataBackup = sharedPref.getString(getString(R.string.storage), "Ville-Température-Vent-Image")
        var mutableList : MutableList<String?> = (dataBackup?.split("-")?.map { it.trim() } ?: return).toMutableList()

        fun fillViews(){
            tvCity.text = mutableList[0]
            tvTemperature.text = "${mutableList[1]}"
            tvWind.text = "${mutableList[2]}"
            Picasso.get().load(mutableList[3])
                .into(imageView)
        }

        fillViews()

        fun getWeatherByCityName(cityName: String) {

            // création d'une instance retrofit
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/weather/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val weatherService = retrofit.create(WeatherService::class.java)

            // appel de l'API
            val result = weatherService.getWeather(cityName)
            result.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val result = response.body()

                        // actualiser la ville
                        val city = result?.get("name")?.asString
                        mutableList[0] = city

                        // actualiser la température
                        val temperature = result?.get("main")?.asJsonObject?.get("temp")?.asString
                        mutableList[1] = "$temperature °C"

                        // actualiser la vitesse du vent
                        val wind = result?.get("wind")?.asJsonObject?.get("speed")?.asString
                        mutableList[2] = "$wind km/h"

                        // actualiser l'image correspondant à la météo
                        val weather = result?.get("weather")?.asJsonArray
                        val icon = weather?.get(0)?.asJsonObject?.get("icon")?.asString
                        mutableList[3] = "https://openweathermap.org/img/w/${icon}.png"

                        // actualiser l'interface
                        fillViews()

                        // convert mutable list to String
                        val separator = "-"
                        val dataString = mutableList.joinToString(separator)

                        // store data in sharedPref
                        with(sharedPref.edit()) {
                            putString(getString(R.string.storage), dataString)
                            apply()
                        }
                    }
                }

                // message d'erreur en cas de non réponse du serveur
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(applicationContext, "Erreur serveur", Toast.LENGTH_SHORT).show()
                }

            })
        }

        // affichage de la météo à la demande
        val btnSearch: Button = findViewById(R.id.btnSearch)
        btnSearch.setOnClickListener() {
            val editCityName: EditText = findViewById(R.id.editCityName)
            val cityName: String = editCityName.text.toString()

            if (cityName.isEmpty()) {
                Toast.makeText(applicationContext, "Champ vide", Toast.LENGTH_SHORT).show()
            } else {
                getWeatherByCityName(cityName)
            }
        }
    }

    // menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.navigation, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // changement d'activité
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }

            R.id.favorites -> {
                val intent = Intent(this, SecondActivity::class.java)
                startActivity(intent)
            }

            else -> Toast.makeText(this, item.title, Toast.LENGTH_LONG).show()
        }

        return true
    }
}