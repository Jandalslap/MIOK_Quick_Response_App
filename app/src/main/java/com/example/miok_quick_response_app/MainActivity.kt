package com.example.miok_quick_response_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup the NavHostFragment
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        NavigationUI.setupActionBarWithNavController(this, navHostFragment.navController)

        // Hide the header and bottom navigation for specific fragments
        navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->

            val header = findViewById<View>(R.id.nav_header)
            val bottomNav = findViewById<View>(R.id.nav_bottom)

            when (destination.id) {
                R.id.splashFragment, R.id.disclaimerFragment -> {
                    supportActionBar?.hide() // Hide the action bar
                    header.isVisible = false // Hide header
                    bottomNav.isVisible = false // Hide bottom navigation
                }
                else -> {
                    supportActionBar?.show() // Show the action bar for other fragments
                    header.isVisible = true // Show header
                    bottomNav.isVisible = true // Show bottom navigation
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        return navController.navController.navigateUp() || super.onSupportNavigateUp()
    }
}


