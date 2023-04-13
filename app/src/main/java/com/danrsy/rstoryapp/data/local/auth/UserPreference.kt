package com.danrsy.rstoryapp.data.local.auth

import android.content.Context
import com.danrsy.rstoryapp.data.model.login.LoginResult

class UserPreference(context: Context) {
    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setUserData(data: LoginResult) {
        val editor = preferences.edit()
        editor.putString(NAME, data.name)
        editor.putString(USER_ID, data.userId)
        editor.putString(TOKEN, data.token)
        editor.apply()
    }

    fun getUser(): LoginResult {
        val name = preferences.getString(NAME, null)
        val userId = preferences.getString(USER_ID, null)
        val token = preferences.getString(TOKEN, null)
        return LoginResult(userId, name, token)
    }

    fun removeUser() {
        val editor = preferences.edit().clear()
        editor.apply()
    }

    companion object {
        private const val PREFS_NAME = "login_pref"
        private const val NAME = "name"
        private const val USER_ID = "userId"
        private const val TOKEN = "token"
    }
}