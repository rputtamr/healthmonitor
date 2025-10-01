package com.example.healthmonitor

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class SymptomsActivity : AppCompatActivity() {

    // Room database instance
    private lateinit var database: AppDatabase

    // Map to store symptom ratings selected by the user
    private val symptomRatings = mutableMapOf<String, Int>()

    // List of symptoms to display in the spinner
    private val symptoms = listOf(
        "Nausea", "Headache", "Diarrhea", "Soar Throat", "Fever",
        "Muscle Ache", "Loss of Smell or Taste", "Cough",
        "Shortness of Breath", "Feeling Tired"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_symptoms)

        // Initialize Room database
        database = AppDatabase.getDatabase(this)

        // Retrieve heart rate and respiratory rate from previous activity
        val heartRate = intent.getIntExtra("HEART_RATE", 0)
        val respiratoryRate = intent.getIntExtra("RESPIRATORY_RATE", 0)

        // Initialize UI elements
        val spinnerSymptom = findViewById<Spinner>(R.id.spinnerSymptom)
        val seekBar = findViewById<SeekBar>(R.id.seekBarRating)
        val tvRating = findViewById<TextView>(R.id.tvRatingValue)
        val btnAddSymptom = findViewById<Button>(R.id.btnAddSymptom)
        val btnUpload = findViewById<Button>(R.id.btnUpload)
        val tvSymptomsList = findViewById<TextView>(R.id.tvSymptomsList)

        // Setup spinner with symptom list
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, symptoms)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSymptom.adapter = adapter

        // Initialize all symptoms with a default rating of 0
        symptoms.forEach { symptomRatings[it] = 0 }

        // Configure SeekBar to show current rating
        seekBar.max = 5
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tvRating.text = progress.toString()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Add selected symptom and rating to the map
        btnAddSymptom.setOnClickListener {
            val selectedSymptom = spinnerSymptom.selectedItem.toString()
            val rating = seekBar.progress
            symptomRatings[selectedSymptom] = rating
            updateSymptomsList(tvSymptomsList)
            Toast.makeText(this, "$selectedSymptom: $rating added", Toast.LENGTH_SHORT).show()
        }

        // Upload data (heart rate, respiratory rate, symptoms) to RoomDB
        btnUpload.setOnClickListener {
            uploadData(heartRate, respiratoryRate)
        }
    }

    // Update TextView to display all added symptoms and their ratings
    private fun updateSymptomsList(tv: TextView) {
        val list = symptomRatings.filter { it.value > 0 }
            .map { "${it.key}: ${it.value}" }
            .joinToString("\n")
        tv.text = if (list.isEmpty()) "No symptoms added" else list
    }


    private fun uploadData(hr: Int, rr: Int) {
        lifecycleScope.launch {
            // Create HealthData object with all measurements
            val entry = HealthData(
                heartRate = hr,
                respiratoryRate = rr,
                nausea = symptomRatings["Nausea"] ?: 0,
                headache = symptomRatings["Headache"] ?: 0,
                diarrhea = symptomRatings["Diarrhea"] ?: 0,
                soarThroat = symptomRatings["Soar Throat"] ?: 0,
                fever = symptomRatings["Fever"] ?: 0,
                muscleAche = symptomRatings["Muscle Ache"] ?: 0,
                lossOfSmellOrTaste = symptomRatings["Loss of Smell or Taste"] ?: 0,
                cough = symptomRatings["Cough"] ?: 0,
                shortnessOfBreath = symptomRatings["Shortness of Breath"] ?: 0,
                feelingTired = symptomRatings["Feeling Tired"] ?: 0,
                timestamp = System.currentTimeMillis()
            )
            // Insert entry into RoomDB
            database.healthDao().insert(entry)
            Toast.makeText(this@SymptomsActivity, "Data uploaded successfully", Toast.LENGTH_SHORT).show()
            // Close activity after successful upload
            finish()
        }
    }
}
