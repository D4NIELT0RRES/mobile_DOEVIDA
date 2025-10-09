package com.example.doevida.model

import com.google.gson.annotations.SerializedName

data class RecuperarSenhaRequest(
    @SerializedName("emailOuUsuario")
    val emailOrUser: String
)


