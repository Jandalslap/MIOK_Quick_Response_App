package com.example.miok_quick_response_app

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Switch
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.miok_info_app.viewmodel.SharedViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

// Main activity that serves as the entry point for the app
class MainActivity : AppCompatActivity() {

    // Access the shared ViewModel scoped to the activity
    private lateinit var sharedViewModel: SharedViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the shared ViewModel
        sharedViewModel = ViewModelProvider(this)[SharedViewModel::class.java]

        // Find the emergency call button
        val emergencyCallButton = findViewById<ImageView>(R.id.emergency_call_button)

        emergencyCallButton.setOnClickListener {
            val dbHelper = ContactDatabase.getInstance(this)
            val emergencyNumber = dbHelper.getEmergencyContact()

            if (emergencyNumber != null) {
                // Start the phone dialer
                val callIntent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:$emergencyNumber")
                }
                startActivity(callIntent)
            } else {
                // No emergency contact found
                Toast.makeText(this, "No emergency contact set.", Toast.LENGTH_SHORT).show()
            }
        }

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
            //updateNavGraphLabels(language) // Uncomment for Fragment Label translation but it reloads app to do it
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
                R.id.splashFragment -> {
                    supportActionBar?.hide() // Hide the action bar
                    header.isVisible = false // Hide header
                    bottomNav.isVisible = false // Hide bottom navigation
                }
                R.id.disclaimerFragment -> {
                    supportActionBar?.hide() // Hide the action bar
                    header.isVisible = true // Show header
                    bottomNav.isVisible = false // Hide bottom navigation
                }
                R.id.editProfileFragment -> {
                    supportActionBar?.show() // Show the action bar
                    header.isVisible = true // Show header
                    bottomNav.isVisible = false // Hide bottom navigation
                }
                R.id.profileFragment -> {
                    supportActionBar?.show() // Show the action bar
                    header.isVisible = true // Show header
                    bottomNav.isVisible = false // Hide bottom navigation
                }
                R.id.contactFragment -> {
                    supportActionBar?.show() // Show the action bar
                    header.isVisible = true // Show header
                    bottomNav.isVisible = false // Hide bottom navigation
                }
                R.id.messageFragment -> {
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

    // Handles the up navigation
    override fun onSupportNavigateUp(): Boolean {
        val navController = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        return navController.navController.navigateUp() || super.onSupportNavigateUp()
    }

    // Function to update nav_graph labels. Causes app to refresh - method not in use.
    private fun updateNavGraphLabels(language: String) {
        // Access the NavHostFragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Access the nav graph
        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)

        // Update the labels based on language
        if (language == "Māori") {
            navGraph.findNode(R.id.homeFragment)?.label = getString(R.string.homeFragment_mr)
            navGraph.findNode(R.id.contactFragment)?.label = getString(R.string.contactFragment_mr)
            navGraph.findNode(R.id.editContactFragment)?.label = getString(R.string.editContactFragment_mr)
            navGraph.findNode(R.id.profileFragment)?.label = getString(R.string.profileFragment_mr)
            navGraph.findNode(R.id.messageFragment)?.label = getString(R.string.messageFragment_mr)
            navGraph.findNode(R.id.quizFragment)?.label = getString(R.string.quizFragment_mr)
            navGraph.findNode(R.id.addContactFragment)?.label = getString(R.string.addContactFragment_mr)
            navGraph.findNode(R.id.quizResultFragment)?.label = getString(R.string.quizResultFragment_mr)
            navGraph.findNode(R.id.editProfileFragment)?.label = getString(R.string.editProfileFragment_mr)
        } else {
            // Default to English
            navGraph.findNode(R.id.homeFragment)?.label = getString(R.string.homeFragment)
            navGraph.findNode(R.id.contactFragment)?.label = getString(R.string.contactFragment)
            navGraph.findNode(R.id.editContactFragment)?.label = getString(R.string.editContactFragment)
            navGraph.findNode(R.id.profileFragment)?.label = getString(R.string.profileFragment)
            navGraph.findNode(R.id.messageFragment)?.label = getString(R.string.messageFragment)
            navGraph.findNode(R.id.quizFragment)?.label = getString(R.string.quizFragment)
            navGraph.findNode(R.id.addContactFragment)?.label = getString(R.string.addContactFragment)
            navGraph.findNode(R.id.quizResultFragment)?.label = getString(R.string.quizResultFragment)
            navGraph.findNode(R.id.editProfileFragment)?.label = getString(R.string.editProfileFragment)
        }

        // Update the navigation graph (without reloading the app)
        navController.graph = navGraph
    }

}


