package com.example.doevida.model

data class ComplementoPerfilRequest(
    val cpf: String,
    val data_nascimento: String, // formato YYYY-MM-DD
    val numero: String, // ou celular para compatibilidade
    val cep: String
)

data class ComplementoPerfilResponse(
    val status: Boolean,
    val status_code: Int,
    val message: String,
    val usuario: Usuario?
)


