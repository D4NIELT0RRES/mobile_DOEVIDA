package com.example.doevida.model

import android.os.Message
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val status: Boolean,
    val status_code: Int,
    val message: String,
    val token: String?,
    val usuario: Usuario?
)

data class Usuario(
    @SerializedName("id") val id: Int,
    @SerializedName("nome") val nome: String,
    @SerializedName("email") val email: String
)
