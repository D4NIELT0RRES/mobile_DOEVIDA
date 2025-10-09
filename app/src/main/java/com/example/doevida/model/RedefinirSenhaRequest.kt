package com.example.doevida.model

data class RedefinirSenhaRequest(
    val emailOrUser: String,
    val newPassword: String
)

