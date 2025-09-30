package com.example.doevida.model

data class RespostaCadastro(

    val status: Boolean,
    val status_code: Int,
    val message: String,
    val usuario: Cadastro? = null
)
