package com.danrsy.rstoryapp.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.danrsy.rstoryapp.R
import com.danrsy.rstoryapp.data.local.auth.UserPreference
import com.danrsy.rstoryapp.data.model.login.LoginResult
import com.danrsy.rstoryapp.databinding.ActivityLoginBinding
import com.danrsy.rstoryapp.ui.MainActivity
import com.danrsy.rstoryapp.ui.register.RegisterActivity
import com.danrsy.rstoryapp.utils.Resource
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginPreference: UserPreference
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginPreference = UserPreference(this)
        checkAuth()

        binding.apply {
            btnRegister.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
            btnLogin.setOnClickListener {
                login()
            }
        }
    }

    private fun login() {
        val email = binding.tfEmail.text.toString().trim()
        val password = binding.tfPassword.text.toString().trim()

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, R.string.empty_login, Toast.LENGTH_SHORT).show()
        } else {
            CoroutineScope(Dispatchers.Main).launch {
                loginViewModel.login(email, password).observe(this@LoginActivity) {
                    when (it) {
                        is Resource.Loading -> showLoadingState(true)
                        is Resource.Success -> {
                            showLoadingState(false)
                            it.data?.result?.let { it1 -> loginPreference.setUserData(it1) }

                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        }
                        is Resource.Error -> {
                            showLoadingState(false)
                            MaterialAlertDialogBuilder(this@LoginActivity)
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

    private fun checkAuth() {
        val tempData = loginPreference.getUser()
        if (tempData.name != null && tempData.userId != null && tempData.token != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
    private fun showLoadingState(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}