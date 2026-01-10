package com.example.loginapp.data.api

import com.example.loginapp.data.model.LoginRequest
import com.example.loginapp.data.model.LoginResponse
import com.example.loginapp.data.model.Trip
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("api/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
    
    @GET("api/viajes/misViajes")
    suspend fun getMyTrips(@Header("Authorization") token: String): Response<List<Trip>>
}
