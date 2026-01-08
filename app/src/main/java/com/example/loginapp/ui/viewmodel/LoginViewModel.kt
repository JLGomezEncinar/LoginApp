package com.example.loginapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loginapp.data.api.RetrofitClient
import com.example.loginapp.data.model.LoginRequest
import com.example.loginapp.data.repository.LoginRepository
import kotlinx.coroutines.launch

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.loginapp.data.local.TokenManager

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = LoginRepository(RetrofitClient.instance)

    private val _loginResult = MutableLiveData<Result<String>>()
    val loginResult: LiveData<Result<String>> = _loginResult

    fun login(email: String, pass: String) {
        viewModelScope.launch {
            try {
                val response = repository.login(LoginRequest(email, pass))
                Log.i ("Prueba", "${email}" + "${pass}")
                if (response.isSuccessful && response.body() != null) {
                    val token = response.body()!!.token
                    TokenManager(getApplication()).saveToken(token)
                    _loginResult.value = Result.success(token)
                } else {
                    _loginResult.value = Result.failure(Exception("Unauthorized"))
                }
                Log.i("Prueba","$response")
            } catch (e: Exception) {
                _loginResult.value = Result.failure(e)
            }
        }
    }
}
