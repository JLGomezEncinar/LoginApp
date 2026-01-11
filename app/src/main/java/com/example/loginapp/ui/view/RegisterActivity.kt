package com.example.loginapp.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.loginapp.databinding.ActivityRegisterBinding
import com.example.loginapp.ui.viewmodel.RegisterViewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSpinners()
        setupListeners()
        observeViewModel()
    }

    private fun setupSpinners() {
        // Tabaco options
        val tabacoOptions = arrayOf(
            "Prefiero no decirlo",
            "Soy fumador",
            "Tolero el humo",
            "No soporto el tabaco"
        )
        val tabacoAdapter = android.widget.ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            tabacoOptions
        )
        tabacoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerTabaco.adapter = tabacoAdapter

        // Mascota options
        val mascotaOptions = arrayOf(
            "Prefiero no decirlo",
            "Tengo un animal de asistencia",
            "Viajo siempre con mi mascota",
            "Tolero a los animales",
            "No soporto a los animales"
        )
        val mascotaAdapter = android.widget.ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            mascotaOptions
        )
        mascotaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerMascota.adapter = mascotaAdapter
    }

    private fun setupListeners() {
        binding.btnAceptar.setOnClickListener {
            val nombre = binding.etNombre.text.toString()
            val alias = binding.etAlias.text.toString()
            val correo = binding.etCorreo.text.toString()
            val password = binding.etPassword.text.toString()
            val tabaco = binding.spinnerTabaco.selectedItemPosition
            val mascota = binding.spinnerMascota.selectedItemPosition
            
            binding.progressBar.visibility = View.VISIBLE
            viewModel.register(nombre, alias, correo, password, tabaco, mascota)
        }

        binding.btnCancelar.setOnClickListener {
            // Navigate back to login without making backend request
            finish()
        }
    }

    private fun observeViewModel() {
        viewModel.registerResult.observe(this) { result ->
            binding.progressBar.visibility = View.GONE
            if (result.isSuccess) {
                val mensaje = result.getOrNull() ?: ""
                Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
                
                // If message is "Usuario registrado", navigate back to login
                if (mensaje == "Usuario registrado") {
                    finish()
                }
                // Otherwise, stay on this screen
            } else {
                Toast.makeText(this, result.exceptionOrNull()?.message ?: "Error desconocido", Toast.LENGTH_LONG).show()
            }
        }
    }
}
