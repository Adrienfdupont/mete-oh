package com.example.mete_oh

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView

class SecondActivity : AppCompatActivity() {

    // déclarer la BottomNavigationView
    lateinit var bottomNav : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        // déclarer la bottomNav
        bottomNav = findViewById(R.id.bottomNav)

        // indiquer l'icône à sélectionner dans la bottomNav
        bottomNav.menu.findItem(R.id.favorites).isChecked = true

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

        // récupérer les favoris
        val sharedPref = this?.getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE) ?: return
        val cityName = sharedPref.getString(getString(R.string.storage), "Vide")
        val writeBox: TextView = findViewById(R.id.retrievedCityName)
        writeBox.text = cityName
    }
}