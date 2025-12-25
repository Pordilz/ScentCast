package com.iu.scentcast

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

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

        // 3. Trigger the Logic (Fetch Weather)
        viewModel.fetchWeatherAndRecommend()

        // 4. Observe Weather Data (Updates automatically when data arrives)
        viewModel.currentTemp.observe(this) { temp ->
            tvWeather.text = "${Constants.MY_CITY}: ${temp}°C"

            // Once we have temp, we try to recommend from the current list
            viewModel.allFragrances.value?.let { collection ->
                viewModel.recommendScent(temp, collection)
            }
        }

        // 5. Observe Recommendation Data
        viewModel.recommendation.observe(this) { fragrance ->
            if (fragrance != null) {
                tvRecommendation.text = fragrance.name
                tvNotes.text = "${fragrance.house} • ${fragrance.notes}"
            } else {
                tvRecommendation.text = "No suitable scent found\n(or collection empty)"
                tvNotes.text = "--"
            }
        }

        // 6. Observe Collection Changes (Retry recommendation if you just added a bottle)
        viewModel.allFragrances.observe(this) { collection ->
            val temp = viewModel.currentTemp.value ?: 20.0
            viewModel.recommendScent(temp, collection)
        }

        // Observe Discovery
        viewModel.discovery.observe(this) { text ->
            tvDiscovery.text = text
        }

        // Observe Timer
        viewModel.timeSinceSpray.observe(this) { time ->
            tvTimer.text = time
            // Visual cue: Make text Red if it says "Reapply"
            if (time.contains("Reapply")) {
                tvTimer.setTextColor(getColor(R.color.brand_cyan))
            } else {
                tvTimer.setTextColor(getColor(R.color.text_secondary))
            }
        }

        // 7. Button Actions
        btnAdd.setOnClickListener {
            // Navigate to the Add Screen
            val intent = Intent(this, AddFragranceActivity::class.java)
            startActivity(intent)
        }

        btnCollection.setOnClickListener {
            val intent = Intent(this, CollectionActivity::class.java)
            startActivity(intent)
        }

        // Button Logic
        btnSpray.setOnClickListener {
            viewModel.logSprayTime()
        }

    }
}