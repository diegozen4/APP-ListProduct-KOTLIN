package com.app.dhpapp.activities.auth

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.app.dhpapp.R
import com.app.dhpapp.activities.core.ProductListActivity
import com.app.dhpapp.model.User
import com.app.dhpapp.repository.LoginRepository
import com.app.dhpapp.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var loginButton: Button
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var loginRepository: LoginRepository
    private lateinit var registerTextView: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        loginButton = findViewById(R.id.loginButton)
        registerTextView = findViewById(R.id.registerTextView)

        loginRepository = LoginRepository(applicationContext) // Pasar el contexto de la aplicación
        loginViewModel = LoginViewModel(loginRepository)

        registerTextView.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            val user = User(email, password, "")
            loginViewModel.loginUser(user,
                onSuccess = { newUser ->
                    // Mostrar datos del usuario
                    Toast.makeText(this@LoginActivity, "¡Bienvenido!", Toast.LENGTH_SHORT).show()

                    // Crear el Intent para iniciar ProductListActivity
                    val intent = Intent(this@LoginActivity, ProductListActivity::class.java).apply {
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
