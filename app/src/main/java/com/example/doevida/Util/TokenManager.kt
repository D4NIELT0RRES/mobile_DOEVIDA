package com.example.doevida.util

import android.content.Context
import android.content.SharedPreferences

class TokenManager(private val context: Context) {

    companion object {
        private const val PREF_NAME = "doevida_prefs"
        private const val TOKEN_KEY = "jwt_token"
        private const val OLD_PREF_NAME = "MyRecipeAppPrefs" // O nome do arquivo conflitante
    }

    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        // Ação definitiva: Deleta o arquivo de preferências antigo para resolver o conflito.
        context.deleteSharedPreferences(OLD_PREF_NAME)
        
        // Salva o novo token no arquivo correto.
        prefs.edit().putString(TOKEN_KEY, token).apply()
    }

    fun getToken(): String? {
        return prefs.getString(TOKEN_KEY, null)
    }

    fun clearToken() {
        // Limpa o token do arquivo de preferências correto.
        prefs.edit().clear().apply() // Limpa todos os dados da sessão (token, nome, cpf, etc)
        
        // Também garante que o arquivo antigo seja limpo no logout.
        context.deleteSharedPreferences(OLD_PREF_NAME)
    }

    fun hasToken(): Boolean {
        return getToken() != null
    }
}