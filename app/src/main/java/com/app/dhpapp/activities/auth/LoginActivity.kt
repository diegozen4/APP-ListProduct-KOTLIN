package com.app.dhpapp.activities.auth

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.app.dhpapp.R
import com.app.dhpapp.activities.core.MainActivity
import com.app.dhpapp.model.User
import com.app.dhpapp.repository.LoginRepository
import com.app.dhpapp.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var loginButton: Button
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var loginRepository: LoginRepository

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        loginButton = findViewById(R.id.loginButton)

        loginRepository = LoginRepository(applicationContext) // Pasar el contexto de la aplicación
        loginViewModel = LoginViewModel(loginRepository)

        loginButton.setOnClickListener {
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            val user = User(email, password, "")
            loginViewModel.loginUser(user,
                onSuccess = { newUser ->
                    // Mostrar datos del usuario
                    Toast.makeText(this@LoginActivity, "¡Bienvenido! Tu rol es ${newUser.rol}", Toast.LENGTH_SHORT).show()

                    // Crear el Intent para iniciar MainActivity
                    val intent = Intent(this@LoginActivity, MainActivity::class.java).apply {
                        putExtra("USER_ROL", newUser.rol)
                    }

                    startActivity(intent)
                    finish() // Finalizar LoginActivity para que el usuario no pueda volver atrás
                },
                onError = { error ->
                    Toast.makeText(this@LoginActivity, "Error: $error", Toast.LENGTH_SHORT).show()
                }
            )

        }
    }
}
