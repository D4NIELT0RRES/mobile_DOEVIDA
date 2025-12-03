package com.example.doevida.service

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.doevida.model.DoacaoManual
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object SharedPreferencesUtils {

    private const val PREFS_NAME = "user_prefs"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_USER_NAME = "user_name"
    private const val KEY_USER_EMAIL = "user_email"
    private const val KEY_USER_CPF = "user_cpf"
    private const val KEY_USER_PROFILE_IMAGE_URL = "user_profile_image_url"
    private const val KEY_MANUAL_DONATIONS = "manual_donations"

    private val gson = Gson()

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveUserData(context: Context, userId: Int, userName: String, userEmail: String, userCpf: String) {
        val editor = getSharedPreferences(context).edit()
        editor.putInt(KEY_USER_ID, userId)
        editor.putString(KEY_USER_NAME, userName)
        editor.putString(KEY_USER_EMAIL, userEmail)
        editor.putString(KEY_USER_CPF, userCpf)
        editor.apply()
        Log.i("SharedPreferencesUtils", "UserData salvo: ID=$userId, Nome=$userName, Email=$userEmail, CPF=$userCpf")
    }

    fun getUserId(context: Context): Int {
        return getSharedPreferences(context).getInt(KEY_USER_ID, -1)
    }

    fun getUserName(context: Context): String {
        return getSharedPreferences(context).getString(KEY_USER_NAME, "Usuário") ?: "Usuário"
    }

    fun getUserEmail(context: Context): String {
        return getSharedPreferences(context).getString(KEY_USER_EMAIL, "email@example.com") ?: "email@example.com"
    }

    fun getUserCpf(context: Context): String {
        return getSharedPreferences(context).getString(KEY_USER_CPF, "") ?: ""
    }

    fun saveUserProfileImage(context: Context, imageUrl: String) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(KEY_USER_PROFILE_IMAGE_URL, imageUrl)
        editor.apply()
        Log.i("SharedPreferencesUtils", "URL da imagem de perfil salva: $imageUrl")
    }

    fun getUserProfileImage(context: Context): String? {
        return getSharedPreferences(context).getString(KEY_USER_PROFILE_IMAGE_URL, null)
    }

    /**
     * Salva uma doação manual, evitando duplicatas exatas (mesmo usuário, hospital e data).
     * Se já existir, remove a antiga e adiciona a nova (atualização).
     */
    fun saveManualDonation(context: Context, newDonation: DoacaoManual) {
        val donations = getManualDonations(context).toMutableList()
        
        // Remove duplicatas se houver (mesmo usuário, hospital e data)
        // Isso evita criar "outro card" se o usuário registrar a mesma doação novamente
        val iterator = donations.iterator()
        while (iterator.hasNext()) {
            val existing = iterator.next()
            if (existing.userId == newDonation.userId &&
                existing.hospitalName == newDonation.hospitalName &&
                existing.donationDate == newDonation.donationDate) {
                iterator.remove() // Remove a antiga para substituir pela nova
            }
        }

        // Adiciona a nova no topo
        donations.add(0, newDonation)
        
        val json = gson.toJson(donations)
        getSharedPreferences(context).edit().putString(KEY_MANUAL_DONATIONS, json).apply()
        Log.i("SharedPreferencesUtils", "Doação manual salva. Total: ${donations.size}")
    }

    fun getManualDonations(context: Context): List<DoacaoManual> {
        val json = getSharedPreferences(context).getString(KEY_MANUAL_DONATIONS, null)
        return if (json != null) {
            val type = object : TypeToken<List<DoacaoManual>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }

    fun clearUserData(context: Context) {
        val editor = getSharedPreferences(context).edit()
        editor.clear()
        editor.apply()
        Log.i("SharedPreferencesUtils", "Todos os dados do usuário foram limpos.")
    }
}
