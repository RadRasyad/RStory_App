package com.danrsy.rstoryapp.ui.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.danrsy.rstoryapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}