package com.app.dhpapp.activities.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.app.dhpapp.R
import com.app.dhpapp.model.User
import com.app.dhpapp.repository.RegisterRepository
import com.app.dhpapp.viewmodel.RegisterViewModel

class RegisterActivity : AppCompatActivity() {
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var registerButton: Button
    private lateinit var registerRepository: RegisterRepository
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        registerButton = findViewById(R.id.registerButton)

        registerRepository = RegisterRepository(applicationContext)
        registerViewModel = RegisterViewModel(registerRepository)

        registerButton.setOnClickListener {
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                val user = User(email, password, "")
                registerViewModel.registerUser(
                    user,
                    onSuccess = {
                        Toast.makeText(
                            this@RegisterActivity,
                            "¡Bienvenido! Registro con éxito",
                            Toast.LENGTH_SHORT
                        ).show()

                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    },
                    onError = { error ->
                        Toast.makeText(this@RegisterActivity, "Error: $error", Toast.LENGTH_SHORT)
                            .show()
                    }
                )
            } else {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
