package com.bangkit.tutordonk.utils

import android.content.Context

class SharedPreferencesHelper(context: Context) {

    private val sharedPreferences =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun saveRole(role: String) {
        sharedPreferences.edit().apply {
            putString(KEY_ROLE, role)
            commit()
        }
    }

    fun saveUsername(username: String) {
        sharedPreferences.edit().apply {
            putString(KEY_NAME, username)
            commit()
        }
    }

    fun getToken(): String = sharedPreferences.getString(KEY_TOKEN, null) ?: ""

    fun getName(): String = sharedPreferences.getString(KEY_NAME, null) ?: ""

    fun getRole(): String = sharedPreferences.getString(KEY_ROLE, null) ?: ""

    fun clearData() = sharedPreferences.edit().clear().apply()

    companion object {
        private const val KEY_TOKEN = "token"
        private const val KEY_NAME = "name"
        private const val KEY_ROLE = "role"
    }
}
