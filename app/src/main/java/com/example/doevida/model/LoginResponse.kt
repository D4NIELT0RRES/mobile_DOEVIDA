package com.example.doevida.model

import android.os.Message

data class LoginResponse(
    val status: Boolean,
    val message: String,
    val token: String? = null,
    val usuarioId: Int? = null
)
