package com.example.loginapp.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.loginapp.databinding.ActivityLoginBinding
import com.example.loginapp.ui.viewmodel.LoginViewModel
import com.example.loginapp.data.local.TokenManager

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (TokenManager(this).isTokenValid()) {
            val intent = Intent(this, TokenInfoActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                binding.progressBar.visibility = View.VISIBLE
                viewModel.login(email, password)
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun observeViewModel() {
        viewModel.loginResult.observe(this) { result ->
            binding.progressBar.visibility = View.GONE
            if (result.isSuccess) {
                Toast.makeText(this, "Login Exitoso!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, TokenInfoActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Login Fallido: ${result.exceptionOrNull()?.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
