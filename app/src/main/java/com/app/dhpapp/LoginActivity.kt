package com.app.dhpapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
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

            val user = User(email, password)
            loginViewModel.loginUser(user,
                onSuccess = {
                    // Iniciar MainActivity cuando el inicio de sesión sea exitoso
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish() // Finalizar LoginActivity para que el usuario no pueda volver atrás
                },
                onError = { error ->
                    // Mostrar un Toast con el mensaje de error
                    Toast.makeText(this@LoginActivity, "Error: $error", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}
