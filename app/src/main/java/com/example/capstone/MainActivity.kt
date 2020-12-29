package com.example.capstone

import android.content.Context
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.findNavController

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        navController = findNavController(R.id.nav_host_fragment)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id in
                arrayOf(R.id.homeScreen)) {
                menu.findItem(R.id.delete_history).setVisible(true);
                menu.findItem(R.id.logout).setVisible(true);
                menu.findItem(R.id.settings).setVisible(true);
            } else {
                menu.findItem(R.id.delete_history).setVisible(false);
                menu.findItem(R.id.logout).setVisible(false);
                menu.findItem(R.id.settings).setVisible(false);
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.delete_history -> {
                false
            }
            R.id.logout -> {
                false
            }
            R.id.settings -> {
                false
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}