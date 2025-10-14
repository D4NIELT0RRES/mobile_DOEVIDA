package com.example.doevida.model

data class HospitaisCards(
    val id: Int,
    val nomeHospital: String,
    val endereco: String,
    val telefone: String,
    val cep: String? = null,
    val horario_abertura: String? = null,
    val horario_fechamento: String? = null,
    val convenios: String? = null,
    val capacidade_maxima: Int? = null,
    val foto: String? = null
)