package com.example.loginapp.data.api

import com.example.loginapp.data.model.LoginRequest
import com.example.loginapp.data.model.LoginResponse
import com.example.loginapp.data.model.RegisterRequest
import com.example.loginapp.data.model.RegisterResponse
import com.example.loginapp.data.model.Trip
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("api/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
    
    @POST("api/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>
    
    @GET("api/misViajes")
    suspend fun getMyTrips(@Header("Authorization") token: String): Response<List<Trip>>

    @GET("api/cercanos")
    suspend fun getNearbyTrips(
        @retrofit2.http.Query("lat") latitude: Double,
        @retrofit2.http.Query("lng") longitude: Double,
        @retrofit2.http.Query("radio") radius: Double = 50000.0
    ): Response<List<Trip>>
}
