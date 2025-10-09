package com.example.doevida.service

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesUtils {

    private const val PREFS_NAME = "MyRecipeAppPrefs"
    private const val USER_ID_KEY = "userId"
    private const val KEY_USER_NAME = "userName"
    private const val KEY_USER_EMAIL = "userEmail"


    private fun getSharedPreferences(context: Context): SharedPreferences{
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    //Função para obter o nome e o e-mail do usuário
    fun getUserName(context: Context): String{
        return getSharedPreferences(context).getString(KEY_USER_NAME, "Usuário") ?: "Usuário"
    }

    fun getUserEmail(context: Context): String{
        return getSharedPreferences(context).getString(KEY_USER_EMAIL, "email@example.com") ?: "email@example.com"
    }
}