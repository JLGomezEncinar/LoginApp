package com.example.loginapp.ui.view

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loginapp.R
import com.example.loginapp.data.api.ApiService
import com.example.loginapp.data.repository.TripRepository
import com.example.loginapp.ui.adapter.TripAdapter
import com.example.loginapp.ui.viewmodel.NearbyTripsViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NearbyTripsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorTextView: TextView
    private lateinit var emptyTextView: TextView
    private lateinit var adapter: TripAdapter
    private lateinit var viewModel: NearbyTripsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nearby_trips)

        // Initialize views
        recyclerView = findViewById(R.id.rv_nearby_trips)
        progressBar = findViewById(R.id.pb_loading_nearby)
        errorTextView = findViewById(R.id.tv_error_nearby)
        emptyTextView = findViewById(R.id.tv_empty_nearby)

        // Setup RecyclerView
        adapter = TripAdapter(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Setup ViewModel
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8088/travelTogether/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        val repository = TripRepository(apiService)
        viewModel = NearbyTripsViewModel(repository)

        observeViewModel()

        // Get coordinates from Intent and search
        val latitude = intent.getDoubleExtra("LATITUDE", 0.0)
        val longitude = intent.getDoubleExtra("LONGITUDE", 0.0)
        
        // Only search if coordinates are valid (not 0.0, 0.0 which is off coast of Africa, unlikely but good enough check for now)
        // Or better, just trust the caller.
        viewModel.searchNearbyTrips(latitude, longitude)
    }

    private fun observeViewModel() {
        viewModel.trips.observe(this) { trips ->
            if (trips.isEmpty()) {
                showEmpty()
            } else {
                showTrips(trips)
                adapter.updateTrips(trips)
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(this) { error ->
            error?.let {
                showError(it)
            }
        }
    }

    private fun showTrips(trips: List<com.example.loginapp.data.model.Trip>) {
        recyclerView.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
        errorTextView.visibility = View.GONE
        emptyTextView.visibility = View.GONE
    }

    private fun showError(message: String) {
        recyclerView.visibility = View.GONE
        progressBar.visibility = View.GONE
        errorTextView.visibility = View.VISIBLE
        emptyTextView.visibility = View.GONE
        errorTextView.text = message
    }

    private fun showEmpty() {
        recyclerView.visibility = View.GONE
        progressBar.visibility = View.GONE
        errorTextView.visibility = View.GONE
        emptyTextView.visibility = View.VISIBLE
    }
}
