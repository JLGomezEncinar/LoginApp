package com.example.loginapp.data.repository

import com.example.loginapp.data.api.ApiService
import com.example.loginapp.data.model.Trip
import retrofit2.Response

class TripRepository(private val apiService: ApiService) {
    suspend fun getMyTrips(token: String): Response<List<Trip>> {
        return apiService.getMyTrips("Bearer $token")
    }
}
