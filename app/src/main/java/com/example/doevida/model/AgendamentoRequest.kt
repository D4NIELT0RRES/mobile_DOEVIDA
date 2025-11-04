package com.example.doevida.model

import com.google.gson.annotations.SerializedName

data class AgendamentoRequest(
    @SerializedName("id_hospital")
    val id_hospital: Int,

    @SerializedName("data")
    val data: String, // Formato YYYY-MM-DD

    @SerializedName("hora")
    val hora: String // Formato HH:MM:SS
)
