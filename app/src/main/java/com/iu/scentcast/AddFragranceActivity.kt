package com.iu.scentcast

import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText

/**
 * Activity for adding a new fragrance to the user's collection.
 * Includes form validation and database insertion via [ScentViewModel].
 */
class AddFragranceActivity : AppCompatActivity() {

    private lateinit var viewModel: ScentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_fragrance)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this)[ScentViewModel::class.java]

        // Bind UI Elements
        val etName = findViewById<TextInputEditText>(R.id.etName)
        val etHouse = findViewById<TextInputEditText>(R.id.etHouse)
        val etNotes = findViewById<TextInputEditText>(R.id.etNotes)
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroupSeason)
        val btnSave = findViewById<Button>(R.id.btnSave)

        // Handle the Save button click
        btnSave.setOnClickListener {
            val name = etName.text.toString()
            val house = etHouse.text.toString()
            val notes = etNotes.text.toString()

            // Retrieve the selected season from the RadioGroup
            val selectedId = radioGroup.checkedRadioButtonId
            val radioButton = findViewById<RadioButton>(selectedId)
            val season = radioButton.text.toString()

            // Basic validation: ensure all fields are populated
            if (name.isBlank() || house.isBlank() || notes.isBlank()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Create a new Fragrance object and save it to the database
            val newFragrance = Fragrance(
                name = name,
                house = house,
                notes = notes,
                season = season
            )

            viewModel.addFragrance(newFragrance)

            Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show()
            finish() // Close the activity and return to the previous screen
        }
    }
}