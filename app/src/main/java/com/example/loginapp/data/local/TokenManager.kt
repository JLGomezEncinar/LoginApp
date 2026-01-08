package com.example.loginapp.data.local

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class TokenManager(context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        val editor = prefs.edit()
        editor.putString("USER_TOKEN", token)
        editor.apply()
    }

    fun getToken(): String? {
        return prefs.getString("USER_TOKEN", null)
    }

    fun clearToken() {
        val editor = prefs.edit()
        editor.remove("USER_TOKEN")
        editor.apply()
    }

    fun isTokenValid(): Boolean {
        val token = getToken() ?: return false
        try {
            val parts = token.split(".")
            if (parts.size < 2) return false
            val payload = String(Base64.decode(parts[1], Base64.URL_SAFE), StandardCharsets.UTF_8)
            val jsonObject = JSONObject(payload)
            val exp = jsonObject.optLong("exp")
            if (exp > 0) {
                // Exp is in seconds, convert current time to seconds
                val currentTime = System.currentTimeMillis() / 1000
                return exp > currentTime
            }
        } catch (e: Exception) {
            return false
        }
        return false
    }
}
