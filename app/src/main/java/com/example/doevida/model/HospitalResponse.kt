package com.example.doevida.model

import com.google.gson.annotations.SerializedName

data class HospitalResponse(
    val status: Boolean,
    val status_code: Int,
    // Casa "items" (seu JSON atual) e "quantidade" (seu modelo anterior)
    @SerializedName(value = "items", alternate = ["quantidade"])
    val items: Int,
    val hospitais: List<HospitaisCards>
)

data class HospitalDetailResponse(
    val status: Boolean,
    val status_code: Int,
    val hospital: HospitaisCards
)