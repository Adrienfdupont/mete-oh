package com.example.mete_oh

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
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

        val writeBox: TextView = findViewById(R.id.retrievedCityName)
        val dataBackup = sharedPref.getString(getString(R.string.storage), "Ville-Température-Vent-Image")
        var mutableList : MutableList<String?> = (dataBackup?.split("-")?.map { it.trim() } ?: return).toMutableList()
        writeBox.text = "Dernière ville actualisée : ${mutableList[0]}"
    }

    // menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.navigation,menu)
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