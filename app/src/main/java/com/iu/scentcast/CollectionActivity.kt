package com.iu.scentcast

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Activity that displays the user's entire fragrance collection.
 * Users can view, edit, or delete fragrances from this screen.
 */
class CollectionActivity : AppCompatActivity() {

    private lateinit var viewModel: ScentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collection)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this)[ScentViewModel::class.java]

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)

        // Initialize the adapter with callback functions for Edit and Delete actions
        val adapter = FragranceAdapter(
            onEditClick = { fragrance -> showEditDialog(fragrance) },
            onDeleteClick = { fragrance -> showDeleteConfirmation(fragrance) }
        )

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Observe the fragrance collection from the database and update the list
        viewModel.allFragrances.observe(this) { fragrances ->
            adapter.submitList(fragrances)
        }
    }

    /**
     * Displays a confirmation dialog before deleting a fragrance.
     */
    private fun showDeleteConfirmation(fragrance: Fragrance) {
        AlertDialog.Builder(this)
            .setTitle("Delete Bottle?")
            .setMessage("Are you sure you want to remove ${fragrance.name}?")
            .setPositiveButton("Delete") { _, _ ->
                viewModel.deleteFragrance(fragrance)
                Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    /**
     * Displays a dialog to edit the name of a fragrance.
     * Note: In a production app, this would ideally use a more comprehensive form.
     */
    private fun showEditDialog(fragrance: Fragrance) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Edit Fragrance")

        val input = EditText(this)
        input.setText(fragrance.name)
        builder.setView(input)

        builder.setPositiveButton("Update") { _, _ ->
            val newName = input.text.toString()
            if (newName.isNotBlank()) {
                val updatedFragrance = fragrance.copy(name = newName)
                viewModel.updateFragrance(updatedFragrance)
                Toast.makeText(this, "Updated!", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }
}