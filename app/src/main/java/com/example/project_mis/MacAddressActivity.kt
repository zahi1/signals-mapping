package com.example.project_mis

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_mis.adapter.MacAddressAdapter

/**
 * Activity for managing a list of MAC addresses
 * This activity allows users to add, edit, and delete MAC addresses
 */
class MacAddressActivity : AppCompatActivity() {

    // Adapter for handling the list of MAC addresses in the RecyclerView
    private lateinit var adapter: MacAddressAdapter

    /**
     * Called when the activity is first created
     * Initializes the UI components, sets up RecyclerView, and configures click listeners
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mac_address)

        // Get the EditText for entering MAC addresses
        val inputMacAddress = findViewById<EditText>(R.id.input_mac_address)

        // Get the Button for adding a new MAC address
        val addMacButton = findViewById<Button>(R.id.add_mac_button)

        // Get the RecyclerView for displaying the list of MAC addresses
        val recyclerView = findViewById<RecyclerView>(R.id.mac_recycler_view)

        // Initialize the adapter with delete and edit actions
        adapter = MacAddressAdapter(
            onDelete = { macAddress ->
                // Delete the MAC address from the list
                adapter.deleteMacAddress(macAddress)
            },
            onEdit = { index, macAddress ->
                // Show the dialog to edit the selected MAC address
                showEditDialog(index, macAddress)
            }
        )

        // Set up the RecyclerView's layout manager as a vertical list
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Attach the adapter to the RecyclerView
        recyclerView.adapter = adapter

        // Set up the click listener for the Add MAC Address button
        addMacButton.setOnClickListener {
            // Retrieve the entered MAC address as a string
            val macAddress = inputMacAddress.text.toString()

            // Check if the MAC address input is not empty
            if (macAddress.isNotEmpty()) {
                // Add the new MAC address to the adapter's list and clear the input field
                adapter.addMacAddress(macAddress)
                inputMacAddress.text.clear()
            } else {
                // Show a toast message if the input is empty
                Toast.makeText(this, "Please enter a MAC address", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Shows a dialog for editing an existing MAC address
     * @param index The position of the MAC address in the list
     * @param macAddress The current MAC address to be edited
     */
    private fun showEditDialog(index: Int, macAddress: String) {
        // Create an AlertDialog builder to construct the edit dialog
        val builder = AlertDialog.Builder(this)

        // Set the dialog title
        builder.setTitle("Edit MAC Address")

        // Create an EditText field and set its text to the current MAC address
        val input = EditText(this)
        input.setText(macAddress)

        // Add the EditText as the dialog's content view
        builder.setView(input)

        // Set up the "OK" button in the dialog
        builder.setPositiveButton("OK") { _, _ ->
            // Retrieve the new MAC address entered by the user
            val newMacAddress = input.text.toString()

            // Check if the new MAC address is not empty
            if (newMacAddress.isNotEmpty()) {
                // Update the MAC address in the adapter's list at the specified index
                adapter.updateMacAddress(index, newMacAddress)
            } else {
                // Show a toast message if the new MAC address is empty
                Toast.makeText(this, "MAC address cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        // Set up the "Cancel" button in the dialog to dismiss it without action
        builder.setNegativeButton("Cancel", null)

        // Show the dialog to the user
        builder.show()
    }
}
