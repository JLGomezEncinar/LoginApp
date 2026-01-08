package com.example.loginapp.data.api

import com.example.loginapp.data.model.LoginRequest
import com.example.loginapp.data.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("api/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}
