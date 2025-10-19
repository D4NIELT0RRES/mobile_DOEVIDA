package com.example.doevida.model

import com.google.gson.annotations.SerializedName

data class HospitaisCards(
    val id: Int,
    @SerializedName("nome")
    val nomeHospital: String,
    val telefone: String,
    val cep: String? = null,
    val complemento: String? = null,
    val horario_abertura: String? = null,
    val horario_fechamento: String? = null,
    val convenios: String? = null,
    val capacidade_maxima: Int? = null,
    val foto: String? = null
) {
    // Propriedade computada para gerar endereço
    val endereco: String
        get() = when {
            !cep.isNullOrBlank() && !complemento.isNullOrBlank() -> "$cep - $complemento"
            !cep.isNullOrBlank() -> cep
            !complemento.isNullOrBlank() -> complemento
            else -> "Endereço não informado"
        }
}