package com.example.doevida.model

data class User(
    val id: Int,
    val nome: String,
    val email: String,
    val cpf: String,
    val cep: String,
    val dataNascimento: String,
    val celular: String
)
