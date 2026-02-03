package com.example.loginapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loginapp.data.model.Trip
import com.example.loginapp.data.repository.TripRepository
import kotlinx.coroutines.launch

class NearbyTripsViewModel(private val repository: TripRepository) : ViewModel() {
    
    private val _trips = MutableLiveData<List<Trip>>()
    val trips: LiveData<List<Trip>> = _trips
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error
    
    fun searchNearbyTrips(latitude: Double, longitude: Double, radius: Double = 200000.0) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            try {
                val response = repository.getNearbyTrips(latitude, longitude, radius)
                if (response.isSuccessful) {
                    _trips.value = response.body() ?: emptyList()
                } else {
                    _error.value = "Error searching trips: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Connection error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
