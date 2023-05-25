package com.danrsy.rstoryapp.ui.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import com.danrsy.rstoryapp.data.RAuthRepository

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = RAuthRepository(application)

    fun login(email: String, password: String) = repository.loginUser(email, password).asLiveData()

}