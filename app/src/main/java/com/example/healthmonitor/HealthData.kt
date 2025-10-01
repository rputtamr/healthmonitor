package com.example.healthmonitor

import android.content.Context
import androidx.room.*

// Entity
@Entity(tableName = "health_data")
data class HealthData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val heartRate: Int,
    val respiratoryRate: Int,
    val nausea: Int,
    val headache: Int,
    val diarrhea: Int,
    val soarThroat: Int,
    val fever: Int,
    val muscleAche: Int,
    val lossOfSmellOrTaste: Int,
    val cough: Int,
    val shortnessOfBreath: Int,
    val feelingTired: Int,
    val timestamp: Long
)

// DAO
@Dao
interface HealthDao {
    @Insert
    suspend fun insert(healthData: HealthData)

    @Query("SELECT * FROM health_data ORDER BY timestamp DESC")
    suspend fun getAllData(): List<HealthData>

    @Query("DELETE FROM health_data")
    suspend fun deleteAll()
}

// Database
@Database(entities = [HealthData::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun healthDao(): HealthDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "health_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}