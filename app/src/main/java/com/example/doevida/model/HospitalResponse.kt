package com.example.doevida.model

data class HospitalResponse(
    val status: Boolean,
    val status_code: Int,
    val items: Int,
    val hospitais: List<HospitaisCards>
)

data class HospitalDetailResponse(
    val status: Boolean,
    val status_code: Int,
    val hospital: HospitaisCards
)