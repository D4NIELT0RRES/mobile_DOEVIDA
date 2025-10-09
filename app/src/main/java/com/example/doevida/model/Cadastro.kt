package com.example.doevida.model

data class Cadastro(
    val nome: String = "",
    val email: String = "",
    val senha: String,
    val id_sexo: Int = 0,
    val id_tipo_sanguineo: Int = 0
)
