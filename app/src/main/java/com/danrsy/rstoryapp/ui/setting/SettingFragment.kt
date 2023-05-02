package com.danrsy.rstoryapp.ui.setting

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import com.danrsy.rstoryapp.R
import com.danrsy.rstoryapp.data.local.auth.UserPreference
import com.danrsy.rstoryapp.databinding.FragmentSettingBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding
    private lateinit var loginPreference: UserPreference
    private val viewModel: SettingViewModel by viewModels()
    private var checkedTheme: Int = 2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginPreference = UserPreference(requireActivity())
        initTheme()

        val name = loginPreference.getUser().name

        binding?.apply {
            tvUsername.text = name
            btnLocalization.setOnClickListener {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
            btnTheme.setOnClickListener {
                themeDialog()
            }
            btnLogout.setOnClickListener {
                loginPreference.removeUser()
                requireActivity().finish()
            }

        }
    }

    private fun initTheme() {
        viewModel.getThemeSettings().observe(requireActivity()) { theme: Int ->
            checkedTheme = when (theme) {
                0 -> {
                    0
                }
                1 -> {
                    1
                }
                else -> {
                    2
                }
            }
        }
    }

    private fun themeDialog() {

        val singleItems = arrayOf("Light", "Dark", "Default")

        viewModel.getThemeSettings().observe(requireActivity()) { theme: Int ->
            when (theme) {
                0 -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    viewModel.saveThemeSetting(0)
                    checkedTheme = 0
                }
                1 -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    viewModel.saveThemeSetting(1)
                    checkedTheme = 1
                }
                else -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    viewModel.saveThemeSetting(2)
                    checkedTheme = 2
                }
            }
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.theme_title))
            .setSingleChoiceItems(singleItems, checkedTheme) { _, which ->
                when(which) {
                    0 ->{
                        checkedTheme = 0
                    }
                    1 ->{
                        checkedTheme = 1
                    }
                    2 ->{
                        checkedTheme = 2
                    }
                }
            }
            .setNegativeButton("Cancel") { dialog,_ ->
                dialog.cancel()
            }
            .setPositiveButton(resources.getString(R.string.ok)) { _, _ ->
                when(checkedTheme) {
                    0 ->{
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        viewModel.saveThemeSetting(0)
                    }
                    1 ->{
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        viewModel.saveThemeSetting(1)

                    }
                    2 ->{
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                        viewModel.saveThemeSetting(2)
                    }
                }

            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}