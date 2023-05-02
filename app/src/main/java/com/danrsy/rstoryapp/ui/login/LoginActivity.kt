package com.danrsy.rstoryapp.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import com.danrsy.rstoryapp.R
import com.danrsy.rstoryapp.data.local.auth.UserPreference
import com.danrsy.rstoryapp.databinding.ActivityLoginBinding
import com.danrsy.rstoryapp.ui.MainActivity
import com.danrsy.rstoryapp.ui.register.RegisterActivity
import com.danrsy.rstoryapp.ui.setting.SettingViewModel
import com.danrsy.rstoryapp.utils.Resource
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginPreference: UserPreference
    private val loginViewModel: LoginViewModel by viewModels()
    private val settingViewModel: SettingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginPreference = UserPreference(this)
        initTheme()
        checkAuth()
        playAnimation()

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
        } else if (password.count() < 8) {
            Toast.makeText(this, R.string.error_password, Toast.LENGTH_SHORT).show()
        } else {
            loginViewModel.login(email, password).observe(this@LoginActivity) {
                when (it) {
                    is Resource.Loading -> showLoadingState(true)
                    is Resource.Success -> {
                        showLoadingState(false)
                        it.data?.result?.let { it1 -> loginPreference.setUserData(it1) }
                        Log.d("Login", it.data?.result?.token.toString())
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    }

                    is Resource.Error -> {
                        showLoadingState(false)
                        showError(it.message.toString())
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

    private fun showError(msg: String) {
        MaterialAlertDialogBuilder(this@LoginActivity)
            .setTitle(resources.getString(R.string.error_failed_login_title))
            .setMessage(msg)
            .setPositiveButton(resources.getString(R.string.ok)) { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }

    private fun initTheme() {
        settingViewModel.getThemeSettings().observe(this) { theme: Int ->
            when (theme) {
                0 -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    settingViewModel.saveThemeSetting(0)
                }

                1 -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    settingViewModel.saveThemeSetting(1)
                }

                else -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    settingViewModel.saveThemeSetting(2)
                }
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.illustration, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val tfEmail = ObjectAnimator.ofFloat(binding.tfEmail, View.ALPHA, 1f).setDuration(500)
        val tfPassword = ObjectAnimator.ofFloat(binding.tfPassword, View.ALPHA, 1f).setDuration(500)
        val btnLogin = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)
        val tvOne = ObjectAnimator.ofFloat(binding.tv1, View.ALPHA, 1f).setDuration(500)
        val tvRegister =
            ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(tfEmail, tfPassword, btnLogin, tvOne, tvRegister)
            startDelay = 500
        }.start()
    }


}