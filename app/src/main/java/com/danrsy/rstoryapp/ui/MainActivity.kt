package com.danrsy.rstoryapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.activity.OnBackPressedCallback
import androidx.navigation.ui.setupWithNavController
import com.danrsy.rstoryapp.R
import com.danrsy.rstoryapp.data.local.auth.UserPreference
import com.danrsy.rstoryapp.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var loginPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginPreference = UserPreference(this)

        setSupportActionBar(binding.toolbar)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_story, R.id.navigation_setting, R.id.navigation_maps
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        onBackPressedDispatcher.addCallback(this , object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val state = checkAuth()
                if (state) {
                    finishAffinity()
                } else {
                    finish()
                }
            }
        })

    }

    private fun checkAuth(): Boolean {
        val tempData = loginPreference.getUser()
        return tempData.name != null && tempData.userId != null && tempData.token != null
    }



}