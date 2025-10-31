package com.example.doevida.model

import com.google.gson.annotations.SerializedName

data class RecuperarSenhaResponse(
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("message")
    val message: String
)
