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
import com.example.loginapp.data.local.TokenManager
import com.example.loginapp.data.repository.TripRepository
import com.example.loginapp.ui.adapter.TripAdapter
import com.example.loginapp.ui.viewmodel.MyTripsViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyTripsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorTextView: TextView
    private lateinit var emptyTextView: TextView
    private lateinit var adapter: TripAdapter
    private lateinit var viewModel: MyTripsViewModel
    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_trips)

        // Initialize views
        recyclerView = findViewById(R.id.rv_trips)
        progressBar = findViewById(R.id.pb_loading)
        errorTextView = findViewById(R.id.tv_error)
        emptyTextView = findViewById(R.id.tv_empty)

        // Setup RecyclerView
        adapter = TripAdapter(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Initialize TokenManager
        tokenManager = TokenManager(this)

        // Setup ViewModel
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8088/travelTogether/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        val repository = TripRepository(apiService)
        viewModel = MyTripsViewModel(repository)

        observeViewModel()

        // Load trips
        val token = tokenManager.getToken()
        if (token != null) {
            viewModel.loadTrips(token)
        } else {
            showError("No hay token disponible")
        }
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
