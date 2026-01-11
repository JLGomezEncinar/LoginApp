package com.example.loginapp.data.model

data class RegisterRequest(
    val nombre: String,
    val alias: String,
    val correo: String,
    val password: String,
    val tabaco: Int,
    val mascota: Int
)
