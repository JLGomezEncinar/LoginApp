package com.example.loginapp.data.repository

import com.example.loginapp.data.api.ApiService
import com.example.loginapp.data.model.LoginRequest
import com.example.loginapp.data.model.LoginResponse
import retrofit2.Response

class LoginRepository(private val apiService: ApiService) {
    suspend fun login(request: LoginRequest): Response<LoginResponse> {
        return apiService.login(request)
    }
}
