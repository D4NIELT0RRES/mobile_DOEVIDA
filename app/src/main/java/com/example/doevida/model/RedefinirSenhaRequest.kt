package com.example.doevida.model

data class RedefinirSenhaRequest(
    val codigo: String,      // Campo para o token de 6 dígitos
    val novaSenha: String    // Campo para a nova senha
)

