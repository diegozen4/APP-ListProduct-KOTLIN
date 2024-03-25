package com.app.dhpapp.viewmodel

import androidx.lifecycle.ViewModel
import com.app.dhpapp.model.User
import com.app.dhpapp.repository.LoginRepository

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {
    fun loginUser(user: User, onSuccess: () -> Unit, onError: (String) -> Unit) {
        loginRepository.loginUser(user, onSuccess, onError)
    }
}
