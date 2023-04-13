package com.danrsy.rstoryapp.ui.register

import androidx.lifecycle.ViewModel
import com.danrsy.rstoryapp.data.RStoryRepository

class RegisterViewModel : ViewModel() {

    private val repository = RStoryRepository()

    suspend fun register(name: String, email: String, password: String) =
        repository.registerUser(name, email, password)

}