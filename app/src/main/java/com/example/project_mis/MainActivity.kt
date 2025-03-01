package com.example.project_mis

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * Main activity that serves as the entry point of the application.
 * Sets up a BottomNavigationView to navigate between different activities.
 */
class MainActivity : AppCompatActivity() {

    /**
     * Called when the activity is first created.
     * Sets up the UI and initializes navigation between different activities.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        // Call the superclass's onCreate method to complete activity creation
        super.onCreate(savedInstanceState)

        // Set the content view to the main activity layout
        setContentView(R.layout.activity_main)

        // Find the BottomNavigationView in the layout by its ID
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Set up a listener for item selection in the bottom navigation view
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            // Use a when expression to handle different menu item selections
            when (menuItem.itemId) {
                // If the "MAC Address" navigation item is selected
                R.id.nav_mac_address -> {
                    // Create an Intent to start the MacAddressActivity
                    startActivity(Intent(this, MacAddressActivity::class.java))
                    true  // Return true to indicate the selection was handled
                }

                // If the "Signal Strength" navigation item is selected
                R.id.nav_signal_strength -> {
                    // Create an Intent to start the SignalStrengthActivity
                    startActivity(Intent(this, SignalStrengthActivity::class.java))
                    true  // Return true to indicate the selection was handled
                }

                // If the "Map" navigation item is selected
                R.id.nav_map -> {
                    // Create an Intent to start the MapActivity
                    startActivity(Intent(this, MapActivity::class.java))
                    true  // Return true to indicate the selection was handled
                }

                // If an unknown item is selected, return false
                else -> false
            }
        }
    }
}
