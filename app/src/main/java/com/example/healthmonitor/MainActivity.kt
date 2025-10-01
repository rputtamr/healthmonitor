package com.example.healthmonitor

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    // Room database instance
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the layout file for this activity
        setContentView(R.layout.activity_main)

        // Initialize the Room database instance
        database = AppDatabase.getDatabase(this)

        // Find buttons from the layout by their IDs
        val btnRecord = findViewById<Button>(R.id.btnRecordHealth)
        val btnViewRecords = findViewById<Button>(R.id.btnViewRecords)
        val btnDelete = findViewById<Button>(R.id.btnDeleteAll)

        // Set click listener to start MeasurementActivity
        btnRecord.setOnClickListener {
            startActivity(Intent(this, MeasurementActivity::class.java))
        }

        // Set click listener to start ViewRecordsActivity
        btnViewRecords.setOnClickListener {
            startActivity(Intent(this, ViewRecordsActivity::class.java))
        }

        // Set click listener to delete all health records
        btnDelete.setOnClickListener {
            deleteAllData()
        }
    }

    // Generative AI Used: ChatGPT (OpenAI, Sept 30, 2025)
// Purpose: Guidance on using coroutines safely to delete all RoomDB entries without blocking the UI
// Prompt: "I already wrote MainActivity. How can I safely delete all health records asynchronously when the user taps a button?"
    private fun deleteAllData() {
        lifecycleScope.launch {
            database.healthDao().deleteAll()
            Toast.makeText(this@MainActivity, "All data deleted", Toast.LENGTH_SHORT).show()

        }
    }
}
