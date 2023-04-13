package com.danrsy.rstoryapp.ui.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.danrsy.rstoryapp.R
import com.danrsy.rstoryapp.databinding.ActivityRegisterBinding
import com.danrsy.rstoryapp.ui.login.LoginViewModel
import com.danrsy.rstoryapp.utils.Resource
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnLogin.setOnClickListener {
                finish()
            }
            btnRegister.setOnClickListener {
                register()
            }
        }

    }

    private fun register() {
        val username = binding.tfUsername.text.toString().trim()
        val email = binding.tfEmail.text.toString().trim()
        val password = binding.tfPassword.text.toString().trim()

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(username)) {
            Toast.makeText(this, R.string.empty_login, Toast.LENGTH_SHORT).show()
        } else {
            CoroutineScope(Dispatchers.Main).launch {
                registerViewModel.register(username, email, password)
                    .observe(this@RegisterActivity) {
                        when (it) {
                            is Resource.Loading -> showLoadingState(true)
                            is Resource.Success -> {
                                showLoadingState(false)
                                Toast.makeText(
                                    this@RegisterActivity,
                                    it.message,
                                    Toast.LENGTH_SHORT
                                ).show()

                                finish()
                            }
                            is Resource.Error -> {
                                showLoadingState(false)
                                MaterialAlertDialogBuilder(this@RegisterActivity)
                                    .setTitle(resources.getString(R.string.error_failed_login_title))
                                    .setMessage(it.message)
                                    .setPositiveButton(resources.getString(R.string.ok)) { dialog, _ ->
                                        dialog.cancel()
                                    }
                                    .show()
                            }
                        }
                    }
            }
        }
    }

    private fun showLoadingState(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}