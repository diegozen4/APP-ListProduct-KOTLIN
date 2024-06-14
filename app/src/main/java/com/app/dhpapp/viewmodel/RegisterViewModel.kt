package com.app.dhpapp.viewmodel

import androidx.lifecycle.ViewModel
import com.app.dhpapp.model.User
import com.app.dhpapp.repository.RegisterRepository

class RegisterViewModel(private val registerRepository: RegisterRepository) : ViewModel() {

    fun registerUser(user: User, onSuccess: () -> Unit, onError: (String) -> Unit) {
        registerRepository.registerUser(
            user, onSuccess, onError
        )
    }
}
