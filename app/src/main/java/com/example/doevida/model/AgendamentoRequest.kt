package com.example.doevida.model

import com.google.gson.annotations.SerializedName

data class AgendamentoRequest(
    @SerializedName("id_hospital")
    val id_hospital: Int,
    @SerializedName("data_agendamento")
    val data_agendamento: String, // Formato YYYY-MM-DD
    @SerializedName("horario_agendamento")
    val horario_agendamento: String // Formato HH:MM
)
