package com.example.project_mis

// Import necessary classes and annotations for Room and Retrofit functionalities
import androidx.room.Entity
import androidx.room.PrimaryKey
import retrofit2.http.GET
import retrofit2.http.Path
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Database
import androidx.room.RoomDatabase

// Retrofit interface for API endpoints related to signal strength and coordinates
interface SignalStrengthService {
    // GET request to retrieve all entries of signal strength data
    @GET("api/signalStrength")
    suspend fun getSignalStrengthMap(): List<SignalStrengthEntry>

    // GET request to retrieve all entries of coordinates data
    @GET("api/allCoordinates")
    suspend fun getAllCoordinates(): List<Metavimai>

    // GET request to retrieve coordinates for a specific `matavimasId`
    @GET("api/getCoordinates/{matavimasId}")
    suspend fun getCoordinatesForMetavimasId(@Path("matavimasId") metavimasId: Int): Metavimai?
}

// Data class to represent a signal strength entry, typically fetched from the API
data class SignalStrengthEntry(
    val id: Int,           // Unique identifier for each signal strength entry
    val matavimas: Int,    // Foreign key linking to a specific coordinate set
    val stiprumas: Int,    // Strength of the signal
    val sensorius: String  // Sensor identifier or name
)

// Room Entity for the `stiprumai` table to store signal strength data locally
@Entity(tableName = "stiprumai")
data class Stiprumai(
    @PrimaryKey val id: Int,     // Primary key for the table
    val matavimas: Int,          // Foreign key reference to the `matavimai` table
    val stiprumas: Int,          // Signal strength value
    val sensorius: String        // Sensor name or identifier
)

// Room Entity for the `matavimai` table to store coordinate data locally
@Entity(tableName = "matavimai")
data class Metavimai(
    @PrimaryKey val matavimas: Int,  // Primary key for each measurement set
    val x: Int,                      // X-coordinate for the location
    val y: Int,                      // Y-coordinate for the location
    val atstumas: Float              // Distance measurement or additional metric
)

// Data class representing a combination of `stiprumai` and `matavimai` data, typically used in the application
data class StiprumaiMatavimai(
    val stiprumaiId: Int,    // ID of the signal strength entry
    val matavimas: Int,      // Reference ID to the coordinates
    val stiprumas: Int,      // Signal strength value
    val sensorius: String,   // Sensor name or identifier
    val x: Int?,             // X-coordinate from the associated `matavimai` entry
    val y: Int?,             // Y-coordinate from the associated `matavimai` entry
    val atstumas: Float?     // Distance metric from the associated `matavimai` entry
)

// Data Access Object (DAO) for accessing and manipulating `stiprumai` table data
@Dao
interface StiprumaiDao {
    // Query to fetch all entries in the `stiprumai` table by a specific `matavimasId`
    @Query("SELECT * FROM stiprumai WHERE matavimas = :matavimasId")
    fun getStrengthByMatavimasId(matavimasId: Int): List<Stiprumai>
}

// Data Access Object (DAO) for accessing and manipulating `matavimai` table data
@Dao
interface MatavimaiDao {
    // Query to fetch a specific `matavimai` entry by its `matavimasId`
    @Query("SELECT * FROM matavimai WHERE matavimas = :matavimasId")
    fun getCoordinatesByMatavimasId(matavimasId: Int): Metavimai?
}

// Room Database abstract class representing the database that combines `stiprumai` and `matavimai` tables
@Database(entities = [Stiprumai::class, Metavimai::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    // Abstract functions to retrieve DAO instances for `stiprumai` and `matavimai` tables
    abstract fun stiprumaiDao(): StiprumaiDao
    abstract fun matavimaiDao(): MatavimaiDao
}
