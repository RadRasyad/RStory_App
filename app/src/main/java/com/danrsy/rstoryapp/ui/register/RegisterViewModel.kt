package com.danrsy.rstoryapp.ui.register

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import com.danrsy.rstoryapp.data.RStoryRepository

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = RStoryRepository(application)

    fun register(name: String, email: String, password: String) =
        repository.registerUser(name, email, password).asLiveData()

}