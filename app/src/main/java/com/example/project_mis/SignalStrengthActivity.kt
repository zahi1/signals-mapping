package com.example.project_mis

// Imports necessary Android and Kotlin classes and libraries for this activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.pow
import kotlin.math.sqrt

// Data class representing a strength point with an ID, x-coordinate, and y-coordinate
data class StrengthPoint(val id: Int, val x: Int, val y: Int) : java.io.Serializable

class SignalStrengthActivity : AppCompatActivity() {

    // Adapter for ListView to manage and display a list of strength points
    private lateinit var strengthListAdapter: ArrayAdapter<String>
    // TextView to display the results of the nearest neighbor calculations
    private lateinit var joinedDataTextView: TextView
    // Mutable list to store user-entered strength points as strings
    private val strengthPoints = mutableListOf<String>()
    // Mutable list to store calculated coordinates for each nearest match
    private val calculatedCoordinates = mutableListOf<StrengthPoint>()
    // Counter to assign unique IDs to each point for identification
    private var pointIdCounter = 1

    // The main entry point for the activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Sets the layout resource for this activity
        setContentView(R.layout.activity_signal_strength)

        // References UI elements from the layout
        val strengthInput1 = findViewById<EditText>(R.id.strengthInput1)
        val strengthInput2 = findViewById<EditText>(R.id.strengthInput2)
        val strengthInput3 = findViewById<EditText>(R.id.strengthInput3)
        val addStrengthBtn = findViewById<Button>(R.id.addStrengthButton)
        val calculateBtn = findViewById<Button>(R.id.calculateButton)
        val strengthListView = findViewById<ListView>(R.id.strengthListView)
        val viewMapBtn = findViewById<Button>(R.id.viewMapButton)
        // Initialize joinedDataTextView for displaying results
        joinedDataTextView = findViewById(R.id.joinedDataTextView)

        // Initializes the ListView adapter for displaying entered strength points
        strengthListAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, strengthPoints)
        // Sets the adapter to the ListView to handle and display items
        strengthListView.adapter = strengthListAdapter

        // Sets a click listener on "Add Strength" button to add new strength points
        addStrengthBtn.setOnClickListener {
            // Retrieves the text entered by the user in input fields
            val point1 = strengthInput1.text.toString()
            val point2 = strengthInput2.text.toString()
            val point3 = strengthInput3.text.toString()

            // Checks if all input fields are filled
            if (point1.isNotEmpty() && point2.isNotEmpty() && point3.isNotEmpty()) {
                // Formats the strength points as a comma-separated string
                val strengthEntry = "$point1, $point2, $point3"
                // Adds the formatted entry to the list of strength points
                strengthPoints.add(strengthEntry)
                // Notifies the adapter to update the ListView display
                strengthListAdapter.notifyDataSetChanged()
                // Clears input fields for the next entry
                strengthInput1.text.clear()
                strengthInput2.text.clear()
                strengthInput3.text.clear()
            } else {
                // Shows a message if any input field is empty
                Toast.makeText(this, "Enter all three strength points", Toast.LENGTH_SHORT).show()
            }
        }

        // Adds a long-click listener on ListView items to allow editing or removal
        strengthListView.setOnItemLongClickListener { _, _, position, _ ->
            // Retrieves the selected point based on the clicked position
            val selectedPoint = strengthPoints[position]
            // Array of options for editing or removing the entry
            val options = arrayOf("Edit", "Remove")
            // Creates an AlertDialog to display the options
            val builder = AlertDialog.Builder(this)
            builder.setItems(options) { _, which ->
                when (which) {
                    // Calls the edit function if "Edit" is selected
                    0 -> editStrength(position, selectedPoint)
                    // Calls the remove function if "Remove" is selected
                    1 -> removeStrength(position)
                }
            }
            // Displays the AlertDialog
            builder.show()
            true
        }

        // Sets up the "Calculate" button to start finding nearest neighbor coordinates
        calculateBtn.setOnClickListener {
            if (strengthPoints.isNotEmpty()) {
                // Starts a coroutine on the I/O dispatcher to run background calculations
                CoroutineScope(Dispatchers.IO).launch {
                    findNearestNeighborCoordinates()
                }
            } else {
                // Shows a message if no strength points are available
                Toast.makeText(this, "Please add strength points", Toast.LENGTH_SHORT).show()
            }
        }

        // Sets up "View Map" button to open a map with calculated coordinates
        viewMapBtn.setOnClickListener {
            if (calculatedCoordinates.isNotEmpty()) {
                // Prepares an Intent to navigate to MapActivity
                val intent = Intent(this, MapActivity::class.java)
                // Attaches the calculated coordinates to the Intent
                intent.putExtra("coordinates", ArrayList(calculatedCoordinates))
                startActivity(intent)
            } else {
                // Shows a message if there are no coordinates to display on the map
                Toast.makeText(this, "No coordinates to show. Please calculate first.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Function to remove a specific strength point by position
    private fun removeStrength(position: Int) {
        // Removes the entry from the list at the specified position
        strengthPoints.removeAt(position)
        // Notifies the adapter to refresh the ListView
        strengthListAdapter.notifyDataSetChanged()
    }

    // Function to edit a specific strength point entry
    private fun editStrength(position: Int, selectedPoint: String) {
        // Splits the selected entry into individual strength values
        val parts = selectedPoint.split(", ")
        if (parts.size == 3) {
            // Sets the input fields with values from the selected entry
            findViewById<EditText>(R.id.strengthInput1).setText(parts[0])
            findViewById<EditText>(R.id.strengthInput2).setText(parts[1])
            findViewById<EditText>(R.id.strengthInput3).setText(parts[2])
            // Removes the old entry to replace it with the edited one
            removeStrength(position)
        }
    }

    // Coroutine function to find nearest neighbor coordinates for each strength point
    private suspend fun findNearestNeighborCoordinates() {
        // Fetches combined strength map data and coordinates from the server API
        val combinedEntries = RetrofitInstance.api.getSignalStrengthMap()
        val coordinatesEntries = RetrofitInstance.api.getAllCoordinates()
        // Clears previously calculated coordinates
        calculatedCoordinates.clear()
        // StringBuilder to accumulate the calculation results
        val results = StringBuilder()

        // Iterates over each user-entered strength point
        for (userStrengthPoints in strengthPoints) {
            // Parses user strength points to integers for comparison
            val userValues = userStrengthPoints.split(",").map { it.trim().toIntOrNull() ?: 0 }

            // Tracks the nearest distance and coordinates
            var nearestDistance = Double.MAX_VALUE
            var nearestCoordinates: Pair<Int, Int>? = null

            // Iterates through each unique `matavimas` ID to find the closest match
            for (matavimasId in combinedEntries.map { it.matavimas }.distinct()) {
                // Filters entries by the current `matavimas` ID
                val entries = combinedEntries.filter { it.matavimas == matavimasId }

                // Ensures exactly 3 strength points are associated with this `matavimas` ID
                if (entries.size >= 3) {
                    // Extracts the first 3 strength values for comparison
                    val entryValues = listOf(entries[0].stiprumas, entries[1].stiprumas, entries[2].stiprumas)
                    // Calculates the Euclidean distance between user input and entry values
                    val distance = calculateEuclideanDistance(userValues, entryValues)

                    // Updates nearest distance and coordinates if a closer match is found
                    if (distance < nearestDistance) {
                        nearestDistance = distance
                        val coordinates = coordinatesEntries.find { it.matavimas == matavimasId }
                        if (coordinates != null) {
                            nearestCoordinates = Pair(coordinates.x, coordinates.y)
                        }
                    }
                }
            }

            // Adds results for the nearest match or indicates if no match is found
            if (nearestCoordinates != null) {
                // Assigns a unique ID to the found coordinates
                val pointId = pointIdCounter++
                calculatedCoordinates.add(StrengthPoint(pointId, nearestCoordinates.first, nearestCoordinates.second))
                results.append("ID: $pointId -> Nearest Neighbor for [$userStrengthPoints]: Coordinates: X: ${nearestCoordinates.first}, Y: ${nearestCoordinates.second}\n")
            } else {
                results.append("No nearest match found for [$userStrengthPoints]\n")
            }
        }

        // Updates the UI with the results on the main thread
        withContext(Dispatchers.Main) {
            joinedDataTextView.text = results.toString()
        }
    }

    // Calculates the Euclidean distance between two lists of integer values
    private fun calculateEuclideanDistance(userValues: List<Int>, entryValues: List<Int>): Double {
        // Computes the squared difference between each pair of user and entry values
        return userValues.zip(entryValues)
            .sumOf { (userValue, entryValue) -> (userValue - entryValue).toDouble().pow(2) }
            .let { sqrt(it) }  // Takes the square root of the sum to get the Euclidean distance
    }
}
