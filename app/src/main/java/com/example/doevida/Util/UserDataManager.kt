package com.example.doevida.util

import android.content.Context

object UserDataManager {

    // Unificado para usar o mesmo arquivo do TokenManager
    private const val PREFS_NAME = "doevida_prefs"
    private const val KEY_USER_NAME = "user_name"
    private const val KEY_USER_EMAIL = "user_email"
    private const val KEY_USER_CPF = "user_cpf"

    private fun getPrefs(context: Context) = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveUser(context: Context, name: String, email: String) {
        getPrefs(context).edit().apply {
            putString(KEY_USER_NAME, name)
            putString(KEY_USER_EMAIL, email)
            apply()
        }
    }

    fun saveCpf(context: Context, cpf: String) {
        getPrefs(context).edit().apply {
            putString(KEY_USER_CPF, cpf)
            apply()
        }
    }

    fun getUserName(context: Context): String {
        return getPrefs(context).getString(KEY_USER_NAME, "") ?: ""
    }

    fun getUserCpf(context: Context): String {
        return getPrefs(context).getString(KEY_USER_CPF, "") ?: ""
    }
}
