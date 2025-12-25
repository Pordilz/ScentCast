package com.iu.scentcast

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CollectionActivity : AppCompatActivity() {

    private lateinit var viewModel: ScentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collection) // We will create this layout next

        // 1. Setup RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = FragranceAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 2. Setup ViewModel
        viewModel = ViewModelProvider(this)[ScentViewModel::class.java]

        // 3. Observe Data (Automatic Updates)
        viewModel.allFragrances.observe(this) { fragrances ->
            // Update the cached copy of the fragrances in the adapter.
            adapter.submitList(fragrances)
        }
    }
}