package com.example.loginapp.data.repository

import com.example.loginapp.data.api.RetrofitClient
import com.example.loginapp.data.model.RegisterRequest
import com.example.loginapp.data.model.RegisterResponse
import retrofit2.Response

class RegisterRepository {
    private val apiService = RetrofitClient.instance

    suspend fun register(
        nombre: String,
        alias: String,
        correo: String,
        password: String,
        tabaco: Int,
        mascota: Int
    ): Response<RegisterResponse> {
        val request = RegisterRequest(nombre, alias, correo, password, tabaco, mascota)
        return apiService.register(request)
    }
}
