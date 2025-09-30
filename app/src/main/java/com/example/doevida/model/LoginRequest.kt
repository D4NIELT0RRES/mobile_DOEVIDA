package com.example.doevida.model

data class LoginRequest(
    val email: String? = null,
    val username: String? = null,
    val senha: String
)
