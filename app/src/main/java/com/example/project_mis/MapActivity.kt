package com.example.project_mis

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * MapActivity displays a custom map with specific coordinates that are passed to it.
 * The coordinates are used to display points on a custom MapView.
 */
class MapActivity : AppCompatActivity() {

    /**
     * Called when the activity is first created.
     * Sets up the UI and loads the coordinates for display on the map.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        // Call the superclass's onCreate method to complete activity creation
        super.onCreate(savedInstanceState)

        // Set the content view to the map activity layout
        setContentView(R.layout.activity_map)

        // Retrieve the list of coordinates from the intent's extras
        // The coordinates are passed as a serializable ArrayList of StrengthPoint objects
        val coordinates: ArrayList<StrengthPoint>? = intent.getSerializableExtra("coordinates") as ArrayList<StrengthPoint>?

        // Find the custom MapView in the layout by its ID
        val mapView = findViewById<MapView>(R.id.mapView)

        // Check if the coordinates are not null
        if (coordinates != null) {
            // Set the coordinates property of the MapView to the retrieved list
            mapView.coordinates = coordinates
        }
    }
}
