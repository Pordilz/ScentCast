package com.iu.scentcast

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

/**
 * Main entry point of the ScentCast application.
 * This activity displays the current weather, recommends a fragrance from the user's collection,
 * provides a discovery suggestion, and tracks application longevity (spray timer).
 */
class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: ScentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Setup ViewModel
        viewModel = ViewModelProvider(this)[ScentViewModel::class.java]

        // 2. Bind UI Elements
        val tvWeather = findViewById<TextView>(R.id.tvWeather)
        val tvRecommendation = findViewById<TextView>(R.id.tvRecommendation)
        val tvNotes = findViewById<TextView>(R.id.tvNotes)
        val btnAdd = findViewById<Button>(R.id.btnAdd)
        val btnCollection = findViewById<Button>(R.id.btnCollection)
        val tvDiscovery = findViewById<TextView>(R.id.tvDiscoveryName)
        val tvTimer = findViewById<TextView>(R.id.tvTimer)
        val btnSpray = findViewById<Button>(R.id.btnSpray)

        // 3. Initial Data Fetch
        viewModel.fetchWeatherAndRecommend()

        // 4. Observers
        
        // Observe Temperature changes and update the weather display
        viewModel.currentTemp.observe(this) { temp ->
            tvWeather.text = "${Constants.MY_CITY}: ${temp}°C"

            // Trigger local recommendation once temperature is available
            viewModel.allFragrances.value?.let { collection ->
                viewModel.recommendScent(temp, collection)
            }
        }

        // Observe Recommendation changes and update the central fragrance display
        viewModel.recommendation.observe(this) { fragrance ->
            if (fragrance != null) {
                tvRecommendation.text = fragrance.name
                tvNotes.text = "${fragrance.house} • ${fragrance.notes}"
            } else {
                tvRecommendation.text = "No suitable scent found\n(or collection empty)"
                tvNotes.text = "--"
            }
        }

        // Observe Collection changes to ensure recommendations stay up-to-date
        viewModel.allFragrances.observe(this) { collection ->
            val temp = viewModel.currentTemp.value ?: 20.0
            viewModel.recommendScent(temp, collection)
        }

        // Observe Discovery engine suggestions
        viewModel.discovery.observe(this) { text ->
            tvDiscovery.text = text
        }

        // Observe Spray Timer (Longevity Tracker)
        viewModel.timeSinceSpray.observe(this) { time ->
            tvTimer.text = time
            // Change color to highlight when it's time to reapply
            if (time.contains("Reapply")) {
                tvTimer.setTextColor(getColor(R.color.brand_cyan))
            } else {
                tvTimer.setTextColor(getColor(R.color.text_secondary))
            }
        }

        // 5. Click Listeners
        
        // Navigate to the "Add Fragrance" screen
        btnAdd.setOnClickListener {
            val intent = Intent(this, AddFragranceActivity::class.java)
            startActivity(intent)
        }

        // Navigate to the "My Collection" screen
        btnCollection.setOnClickListener {
            val intent = Intent(this, CollectionActivity::class.java)
            startActivity(intent)
        }

        // Log a new spray event
        btnSpray.setOnClickListener {
            viewModel.logSprayTime()
        }
    }
}