package com.example.loginapp.ui.view

import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.loginapp.R
import com.example.loginapp.data.local.TokenManager
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.Locale

class TokenInfoActivity : AppCompatActivity() {

    private lateinit var tokenTextView: TextView
    private lateinit var expirationTextView: TextView
    private lateinit var aliasTextView: TextView
    private lateinit var myTripsButton: Button
    private lateinit var locationEditText: EditText
    private lateinit var searchButton: Button
    private lateinit var logoutButton: Button
    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_token_info)

        tokenTextView = findViewById(R.id.tv_token)
        expirationTextView = findViewById(R.id.tv_expiration)
        aliasTextView = findViewById(R.id.tv_alias)
        myTripsButton = findViewById(R.id.btn_my_trips)
        locationEditText = findViewById(R.id.et_location)
        searchButton = findViewById(R.id.btn_search)
        logoutButton = findViewById(R.id.btn_logout)
        tokenManager = TokenManager(this)

        val token = tokenManager.getToken()

        if (token != null) {
            tokenTextView.text = "Token: $token"
            expirationTextView.text = "Expiration: ${getExpirationFromToken(token)}"
            val alias = getAliasFromToken(token)
            aliasTextView.text = "Hola $alias"
        } else {
            tokenTextView.text = "No token found"
            aliasTextView.text = "Hola Usuario"
        }

        myTripsButton.setOnClickListener {
            val intent = Intent(this, MyTripsActivity::class.java)
            startActivity(intent)
        }

        searchButton.setOnClickListener {
            val locationName = locationEditText.text.toString()
            if (locationName.isNotEmpty()) {
                performSearch(locationName)
            } else {
                Toast.makeText(this, "Por favor, introduce una localidad", Toast.LENGTH_SHORT).show()
            }
        }

        logoutButton.setOnClickListener {
            tokenManager.clearToken()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun performSearch(locationName: String) {
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            // Deprecated in API 33 but still widely used/supported for simple use cases or older APIs.
            // For API 33+ there is an async version, but keeping it simple for now as requested.
            // Suppressing deprecation warning would be ideal if we were strict.
            val addresses = geocoder.getFromLocationName(locationName, 1)
            
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                val latitude = address.latitude
                val longitude = address.longitude

                val intent = Intent(this, NearbyTripsActivity::class.java)
                intent.putExtra("LATITUDE", latitude)
                intent.putExtra("LONGITUDE", longitude)
                startActivity(intent)
            } else {
                Toast.makeText(this, "No se encontró la ubicación", Toast.LENGTH_SHORT).show()
            }
        } catch (e: IOException) {
            Toast.makeText(this, "Error de geocodificación: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun getExpirationFromToken(token: String): String {
        try {
            val parts = token.split(".")
            if (parts.size < 2) return "Invalid Token"
            
            val payload = String(Base64.decode(parts[1], Base64.URL_SAFE), StandardCharsets.UTF_8)
            val jsonObject = JSONObject(payload)
            val exp = jsonObject.optLong("exp")
            
            return if (exp > 0) {
                 java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date(exp * 1000))
            } else {
                "No expiration found"
            }
        } catch (e: Exception) {
            return "Error decoding token"
        }
    }

    private fun getAliasFromToken(token: String): String {
        try {
            val parts = token.split(".")
            if (parts.size < 2) return "Invalid Token"

            val payload = String(Base64.decode(parts[1], Base64.URL_SAFE), StandardCharsets.UTF_8)
            val jsonObject = JSONObject(payload)
            return jsonObject.optString("sub", "No alias found")
        } catch (e: Exception) {
            return "Error decoding token"
        }
    }
}
