package com.example.doevida.model

data class Cadastro(
    val nome: String = "",
    val email: String = "",
    val senha: String,
    val confirmarSenha: String
)
