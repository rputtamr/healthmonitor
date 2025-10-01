package com.example.healthmonitor

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MeasurementActivity : AppCompatActivity() {

    // Variables to store measured heart rate and respiratory rate
    private var heartRate: Int = 0
    private var respiratoryRate: Int = 0

    // UI elements
    private lateinit var tvHeartRate: TextView
    private lateinit var tvRespRate: TextView
    private lateinit var btnMeasureHeart: Button
    private lateinit var btnMeasureResp: Button
    private lateinit var btnNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the layout for this activity
        setContentView(R.layout.activity_measurement)

        // Initialize all the UI elements
        initViews()

        // Set click listeners for buttons
        setupListeners()
    }

    // Initialize the views using findViewById
    private fun initViews() {
        tvHeartRate = findViewById(R.id.tvHeartRateValue)
        tvRespRate = findViewById(R.id.tvRespRateValue)
        btnMeasureHeart = findViewById(R.id.btnMeasureHeart)
        btnMeasureResp = findViewById(R.id.btnMeasureResp)
        btnNext = findViewById(R.id.btnNext)
    }

    // Setup click listeners for measurement buttons and navigation
    private fun setupListeners() {
        btnMeasureHeart.setOnClickListener {
            measureHeartRate()
        }

        btnMeasureResp.setOnClickListener {
            measureRespiratoryRate()
        }

        btnNext.setOnClickListener {
            navigateToSymptoms()
        }
    }

    // Function to measure heart rate (currently using random values)
    private fun measureHeartRate() {
        // TODO: Replace with actual sensor measurement code
        heartRate = (60..100).random() // Simulated heart rate
        tvHeartRate.text = "$heartRate bpm"
        Toast.makeText(this, "Heart rate measured", Toast.LENGTH_SHORT).show()
    }

    // Function to measure respiratory rate (currently using random values)
    private fun measureRespiratoryRate() {
        // TODO: Replace with actual sensor measurement code
        respiratoryRate = (12..20).random() // Simulated respiratory rate
        tvRespRate.text = "$respiratoryRate bpm"
        Toast.makeText(this, "Respiratory rate measured", Toast.LENGTH_SHORT).show()
    }

    // Generative AI Used: ChatGPT (OpenAI, Sept 30, 2025)
// Purpose: Guidance on passing multiple measured values between activities safely
// Prompt: "I already wrote MeasurementActivity. How should I pass heart rate and respiratory rate to the next activity without losing data?"
    private fun navigateToSymptoms() {
        if (heartRate > 0 && respiratoryRate > 0) {
            val intent = Intent(this, SymptomsActivity::class.java).apply {
                putExtra("HEART_RATE", heartRate)
                putExtra("RESPIRATORY_RATE", respiratoryRate)
            }
            startActivity(intent)
        } else {
            Toast.makeText(this, "Please measure both rates first", Toast.LENGTH_SHORT).show()
        }
    }

}
