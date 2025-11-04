package com.example.doevida.util

import android.content.Context

object UserDataManager {

    private const val PREFS_NAME = "doevida_prefs"
    private const val KEY_USER_ID = "user_id" // Chave para o ID
    private const val KEY_USER_NAME = "user_name"
    private const val KEY_USER_EMAIL = "user_email"
    private const val KEY_USER_CPF = "user_cpf"

    private fun getPrefs(context: Context) = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // Modificado para salvar o ID do usuário
    fun saveUserData(context: Context, id: Int, name: String, email: String) {
        getPrefs(context).edit().apply {
            putInt(KEY_USER_ID, id)
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

    // Função para obter o ID do usuário
    fun getUserId(context: Context): Int {
        return getPrefs(context).getInt(KEY_USER_ID, 0)
    }

    fun getUserName(context: Context): String {
        return getPrefs(context).getString(KEY_USER_NAME, "") ?: ""
    }

    fun getUserCpf(context: Context): String {
        return getPrefs(context).getString(KEY_USER_CPF, "") ?: ""
    }
}
