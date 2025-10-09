package com.example.doevida.service

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

object SharedPreferencesUtils {

    private const val PREFS_NAME = "MyRecipeAppPrefs"
    private const val KEY_USER_NAME = "userName"
    private const val KEY_USER_EMAIL = "userEmail"

    // Função para obter o SharedPreferences
    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    // Função para salvar nome e e-mail do usuário
    fun saveUserData(context: Context, userName: String, userEmail: String) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(KEY_USER_NAME, userName)
        editor.putString(KEY_USER_EMAIL, userEmail)
        editor.apply()
        Log.i("SharedPreferencesUtils", "UserData salvo: Nome=$userName, Email=$userEmail")
    }

    // Função para obter o nome do usuário
    fun getUserName(context: Context): String {
        return getSharedPreferences(context).getString(KEY_USER_NAME, "Usuário") ?: "Usuário"
    }

    // Função para obter o e-mail do usuário
    fun getUserEmail(context: Context): String {
        return getSharedPreferences(context).getString(KEY_USER_EMAIL, "email@example.com") ?: "email@example.com"
    }
}
