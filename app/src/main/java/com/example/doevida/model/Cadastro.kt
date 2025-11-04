package com.example.doevida.model

import com.google.gson.annotations.SerializedName

data class Cadastro(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("nome")
    val nome: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("senha")
    val senha: String,
    @SerializedName("id_sexo")
    val id_sexo: Int,
    @SerializedName("id_tipo_sanguineo")
    val id_tipo_sanguineo: Int
)
