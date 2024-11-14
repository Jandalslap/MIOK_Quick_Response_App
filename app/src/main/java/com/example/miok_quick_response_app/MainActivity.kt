package com.example.miok_quick_response_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Switch
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.miok_info_app.viewmodel.SharedViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    // Access the shared ViewModel scoped to the activity
    private lateinit var sharedViewModel: SharedViewModel

    // This should be done automaticly by hilt, but this is for debuging.
//    private val viewModel: QuestionsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the shared ViewModel
        sharedViewModel = ViewModelProvider(this)[SharedViewModel::class.java]

        // Find the Switch by its ID
        val languageSwitch = findViewById<Switch>(R.id.languageswitch)
        var language = "English"

        // Set initial text to English
        languageSwitch.text = "English"

        // Set up the toggle listener
        languageSwitch.setOnCheckedChangeListener { _, isChecked ->
            language = if (isChecked) {
                languageSwitch.text = "Māori"
                "Māori"
            } else {
                languageSwitch.text = "English"
                "English"
            }
            // Update language in the shared ViewModel
            sharedViewModel.updateLanguage(language)
        }

        // Setup the NavHostFragment
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        NavigationUI.setupActionBarWithNavController(this, navHostFragment.navController)

        // Setup BottomNavigationView
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    navHostFragment.navController.navigate(R.id.homeFragment)
                    true
                }
                R.id.contactFragment -> {
                    navHostFragment.navController.navigate(R.id.contactFragment)
                    true
                }
                else -> false
            }
        }

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
                R.id.editProfileFragment -> {
                    supportActionBar?.show() // Show the action bar
                    header.isVisible = true // Show header
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


