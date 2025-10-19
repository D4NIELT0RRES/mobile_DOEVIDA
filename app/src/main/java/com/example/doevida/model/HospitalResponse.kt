package com.example.doevida.model

import com.google.gson.annotations.SerializedName

data class HospitalResponse(
    val status: Boolean,
    val status_code: Int,
    @SerializedName("quantidade")
    val items: Int,              // API retorna "quantidade", mapeia para "items"
    val hospitais: List<HospitaisCards>
)

data class HospitalDetailResponse(
    val status: Boolean,
    val status_code: Int,
    val hospital: HospitaisCards
)