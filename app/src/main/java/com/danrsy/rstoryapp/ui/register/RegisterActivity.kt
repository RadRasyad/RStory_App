package com.danrsy.rstoryapp.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.danrsy.rstoryapp.R
import com.danrsy.rstoryapp.databinding.ActivityRegisterBinding
import com.danrsy.rstoryapp.utils.Resource

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playAnimation()

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
        val password = binding.tfPassword.text.toString()

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(username)) {
            Toast.makeText(this, R.string.empty_login, Toast.LENGTH_SHORT).show()
        } else if (password.count() < 8) {
            Toast.makeText(this, R.string.error_password, Toast.LENGTH_SHORT).show()
        } else {
            registerViewModel.register(username, email, password)
                .observe(this@RegisterActivity) {
                    when (it) {
                        is Resource.Loading -> showLoadingState(true)
                        is Resource.Success -> {
                            showLoadingState(false)
                            Toast.makeText(
                                this@RegisterActivity,
                                it.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()
                            finish()
                        }

                        is Resource.Error -> {
                            showLoadingState(false)
                            Toast.makeText(
                                this@RegisterActivity,
                                it.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                }
        }

    }

    private fun showLoadingState(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.illustration, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val tfUsername = ObjectAnimator.ofFloat(binding.tfUsername, View.ALPHA, 1f).setDuration(500)
        val tfEmail = ObjectAnimator.ofFloat(binding.tfEmail, View.ALPHA, 1f).setDuration(500)
        val tfPassword = ObjectAnimator.ofFloat(binding.tfPassword, View.ALPHA, 1f).setDuration(500)
        val tvLogin = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)
        val tvOne = ObjectAnimator.ofFloat(binding.tv1, View.ALPHA, 1f).setDuration(500)
        val btnRegister =
            ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(tfUsername, tfEmail, tfPassword, btnRegister, tvOne, tvLogin)
            startDelay = 500
        }.start()
    }
}