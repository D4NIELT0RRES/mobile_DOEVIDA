package com.example.doevida.service

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

object SharedPreferencesUtils {

    private const val PREFS_NAME = "user_prefs" // Nome mais genérico para as preferências
    private const val KEY_USER_ID = "user_id" // << CHAVE ADICIONADA
    private const val KEY_USER_NAME = "user_name"
    private const val KEY_USER_EMAIL = "user_email"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    // --- FUNÇÃO CORRIGIDA ---
    // Agora aceita e salva o ID do usuário
    fun saveUserData(context: Context, userId: Int, userName: String, userEmail: String) {
        val editor = getSharedPreferences(context).edit()
        editor.putInt(KEY_USER_ID, userId) // << LÓGICA PARA SALVAR O ID
        editor.putString(KEY_USER_NAME, userName)
        editor.putString(KEY_USER_EMAIL, userEmail)
        editor.apply()
        Log.i("SharedPreferencesUtils", "UserData salvo: ID=$userId, Nome=$userName, Email=$userEmail")
    }

    // --- NOVA FUNÇÃO ---
    // Função para obter o ID do usuário
    fun getUserId(context: Context): Int {
        return getSharedPreferences(context).getInt(KEY_USER_ID, -1) // Retorna -1 se não encontrado
    }

    // Função para obter o nome do usuário
    fun getUserName(context: Context): String {
        return getSharedPreferences(context).getString(KEY_USER_NAME, "Usuário") ?: "Usuário"
    }

    // Função para obter o e-mail do usuário
    fun getUserEmail(context: Context): String {
        return getSharedPreferences(context).getString(KEY_USER_EMAIL, "email@example.com") ?: "email@example.com"
    }

    // Função para limpar todos os dados (útil para logout)
    fun clearUserData(context: Context) {
        val editor = getSharedPreferences(context).edit()
        editor.clear()
        editor.apply()
        Log.i("SharedPreferencesUtils", "Todos os dados do usuário foram limpos.")
    }
}
