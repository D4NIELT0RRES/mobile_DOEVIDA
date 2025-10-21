package com.example.doevida.model

import com.google.gson.annotations.SerializedName

data class HospitaisCards(
    val id: Int,

    // Casa tanto "nomeHospital" (seu JSON atual) quanto "nome" (se um dia vier assim)
    @SerializedName(value = "nomeHospital", alternate = ["nome"])
    val nomeHospital: String,

    val telefone: String,

    val cep: String? = null,
    val complemento: String? = null,

    @SerializedName("horario_abertura")
    val horario_abertura: String? = null,

    @SerializedName("horario_fechamento")
    val horario_fechamento: String? = null,

    val convenios: String? = null,

    @SerializedName("capacidade_maxima")
    val capacidade_maxima: Int? = null,

    val foto: String? = null,

    // O back agora envia "endereco"; mantemos opcional
    @SerializedName("endereco")
    private val enderecoApi: String? = null
) {
    val endereco: String
        get() = when {
            !enderecoApi.isNullOrBlank() -> enderecoApi
            !cep.isNullOrBlank() && !complemento.isNullOrBlank() -> "$cep - $complemento"
            !cep.isNullOrBlank() -> cep
            !complemento.isNullOrBlank() -> complemento
            else -> "Endereço não informado"
        }
}

