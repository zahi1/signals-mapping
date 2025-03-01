package com.example.project_mis

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Singleton object that provides a Retrofit instance for making network calls.
 * This object creates a Retrofit instance configured to connect to the specified BASE_URL
 * and uses Gson for JSON serialization/deserialization.
 */
object RetrofitInstance {

    // Base URL for the API server. This should be the root URL of the server.
    private const val BASE_URL = "http://192.168.137.1:3000/"  // Replace with your server's IP and port

    // Lazy initialization of the Retrofit instance
    private val retrofit by lazy {
        // Builds the Retrofit instance with the specified BASE_URL and Gson converter
        Retrofit.Builder()
            .baseUrl(BASE_URL) // Sets the base URL for API calls
            .addConverterFactory(GsonConverterFactory.create()) // Adds Gson converter for JSON parsing
            .build() // Builds the Retrofit instance
    }

    // Lazy initialization of the API interface (SignalStrengthService)
    val api: SignalStrengthService by lazy {
        // Creates an implementation of the SignalStrengthService interface using the Retrofit instance
        retrofit.create(SignalStrengthService::class.java)
    }
}
