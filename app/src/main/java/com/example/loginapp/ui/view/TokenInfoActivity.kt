package com.example.loginapp.ui.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.loginapp.R
import com.example.loginapp.data.local.TokenManager
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class TokenInfoActivity : AppCompatActivity() {

    private lateinit var tokenTextView: TextView
    private lateinit var expirationTextView: TextView
    private lateinit var aliasTextView: TextView
    private lateinit var logoutButton: Button
    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_token_info)

        tokenTextView = findViewById(R.id.tv_token)
        expirationTextView = findViewById(R.id.tv_expiration)
        aliasTextView = findViewById(R.id.tv_alias)
        logoutButton = findViewById(R.id.btn_logout)
        tokenManager = TokenManager(this)

        val token = tokenManager.getToken()

        if (token != null) {
            tokenTextView.text = "Token: $token"
            expirationTextView.text = "Expiration: ${getExpirationFromToken(token)}"
            aliasTextView.text = "Alias: ${getAliasFromToken(token)}"
        } else {
            tokenTextView.text = "No token found"
        }

        logoutButton.setOnClickListener {
            tokenManager.clearToken()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
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
