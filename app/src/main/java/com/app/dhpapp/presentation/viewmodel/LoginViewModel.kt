package com.app.dhpapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.app.dhpapp.domain.repository.LoginRepository
import com.app.dhpapp.model.User

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {
    fun loginUser(user: User, onSuccess: (User) -> Unit, onError: (String) -> Unit) {
        loginRepository.loginUser(user,
            onSuccess = { newUser ->
                onSuccess.invoke(newUser)
            },
            onError = { error ->
                onError.invoke(error)
            }
        )
    }

}
