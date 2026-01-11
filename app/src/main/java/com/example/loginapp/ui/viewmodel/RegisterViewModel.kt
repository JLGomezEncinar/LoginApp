package com.example.loginapp.ui.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loginapp.data.repository.RegisterRepository
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    private val repository = RegisterRepository()
    
    private val _registerResult = MutableLiveData<Result<String>>()
    val registerResult: LiveData<Result<String>> = _registerResult
    
    fun register(nombre: String, alias: String, correo: String, password: String, tabaco: Int, mascota: Int) {
        // Validate all fields are filled
        if (!areFieldsValid(nombre, alias, correo, password)) {
            _registerResult.value = Result.failure(Exception("Todos los campos son obligatorios"))
            return
        }
        
        // Validate email format
        if (!isEmailValid(correo)) {
            _registerResult.value = Result.failure(Exception("El formato del correo no es válido"))
            return
        }
        
        viewModelScope.launch {
            try {
                val response = repository.register(nombre, alias, correo, password, tabaco, mascota)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _registerResult.value = Result.success(responseBody.respuesta)
                    } else {
                        _registerResult.value = Result.failure(Exception("Respuesta vacía del servidor"))
                    }
                } else {
                    _registerResult.value = Result.failure(Exception("Error: ${response.code()}"))
                }
            } catch (e: Exception) {
                _registerResult.value = Result.failure(e)
            }
        }
    }
    
    private fun areFieldsValid(nombre: String, alias: String, correo: String, password: String): Boolean {
        return nombre.isNotBlank() && alias.isNotBlank() && correo.isNotBlank() && password.isNotBlank()
    }
    
    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
