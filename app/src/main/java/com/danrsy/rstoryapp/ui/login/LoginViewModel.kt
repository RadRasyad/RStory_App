package com.danrsy.rstoryapp.ui.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import com.danrsy.rstoryapp.data.RStoryRepository

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = RStoryRepository(application)

    fun login(email: String, password: String) = repository.loginUser(email, password).asLiveData()

}