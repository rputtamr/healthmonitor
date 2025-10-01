package com.example.healthmonitor

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ViewRecordsActivity : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private lateinit var recyclerView: RecyclerView
    private lateinit var tvNoData: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_records)

        database = AppDatabase.getDatabase(this)
        recyclerView = findViewById(R.id.recyclerViewRecords)
        tvNoData = findViewById(R.id.tvNoData)

        recyclerView.layoutManager = LinearLayoutManager(this)

        loadRecords()
    }

    // Load all health records asynchronously and update UI
    // Generative AI Used: ChatGPT (OpenAI, Sept 30, 2025)
    // Purpose: Guidance on fetching RoomDB records asynchronously and handling UI visibility for empty/non-empty lists
    // Prompt: " How should I load Room database records without blocking the UI and show a 'No Data' message if empty?"
    private fun loadRecords() {
        lifecycleScope.launch {
            val records = database.healthDao().getAllData()

            if (records.isEmpty()) {
                tvNoData.visibility = android.view.View.VISIBLE
                recyclerView.visibility = android.view.View.GONE
            } else {
                tvNoData.visibility = android.view.View.GONE
                recyclerView.visibility = android.view.View.VISIBLE
                recyclerView.adapter = HealthRecordsAdapter(records)
            }
        }
    }
}

// Adapter for RecyclerView to display health records
class HealthRecordsAdapter(private val records: List<HealthData>) :
    RecyclerView.Adapter<HealthRecordsAdapter.ViewHolder>() {

    class ViewHolder(view: android.view.View) : RecyclerView.ViewHolder(view) {
        val tvTimestamp: TextView = view.findViewById(R.id.tvTimestamp)
        val tvHeartRate: TextView = view.findViewById(R.id.tvHeartRate)
        val tvRespRate: TextView = view.findViewById(R.id.tvRespRate)
        val tvSymptoms: TextView = view.findViewById(R.id.tvSymptoms)
    }

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): ViewHolder {
        val view = android.view.LayoutInflater.from(parent.context)
            .inflate(R.layout.item_health_record, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val record = records[position]
        val dateFormat = SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault())

        holder.tvTimestamp.text = dateFormat.format(Date(record.timestamp))
        holder.tvHeartRate.text = "Heart Rate: ${record.heartRate} bpm"
        holder.tvRespRate.text = "Respiratory Rate: ${record.respiratoryRate} bpm"

        // Display symptoms only if reported
        val symptoms = buildString {
            if (record.nausea > 0) append("Nausea: ${record.nausea}\n")
            if (record.headache > 0) append("Headache: ${record.headache}\n")
            if (record.diarrhea > 0) append("Diarrhea: ${record.diarrhea}\n")
            if (record.soarThroat > 0) append("Soar Throat: ${record.soarThroat}\n")
            if (record.fever > 0) append("Fever: ${record.fever}\n")
            if (record.muscleAche > 0) append("Muscle Ache: ${record.muscleAche}\n")
            if (record.lossOfSmellOrTaste > 0) append("Loss of Smell/Taste: ${record.lossOfSmellOrTaste}\n")
            if (record.cough > 0) append("Cough: ${record.cough}\n")
            if (record.shortnessOfBreath > 0) append("Shortness of Breath: ${record.shortnessOfBreath}\n")
            if (record.feelingTired > 0) append("Feeling Tired: ${record.feelingTired}\n")
        }

        holder.tvSymptoms.text = if (symptoms.isNotEmpty()) {
            "Symptoms:\n$symptoms"
        } else {
            "No symptoms reported"
        }
    }

    override fun getItemCount() = records.size
}
