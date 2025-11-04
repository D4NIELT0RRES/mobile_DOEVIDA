package com.example.doevida.model

import com.google.gson.annotations.SerializedName

data class HistoricoResponse(
    @SerializedName("agendamentos")
    val agendamentos: List<AgendamentoItem>
)

data class AgendamentoItem(
    @SerializedName("id")
    val id: Int,
    @SerializedName("data")
    val dataAgendamento: String,
    @SerializedName("hora")
    val horarioAgendamento: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("hospital")
    val hospital: HospitalAgendamentoInfo?
)

data class HospitalAgendamentoInfo(
    @SerializedName("nome")
    val nome: String
)
