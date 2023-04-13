package com.danrsy.rstoryapp.ui.login

import androidx.lifecycle.ViewModel
import com.danrsy.rstoryapp.data.RStoryRepository

class LoginViewModel : ViewModel() {

    private val repository = RStoryRepository()

    suspend fun login(email: String, password: String) = repository.loginUser(email, password)

}