package com.example.doevida.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HistoricoDoacaoCombinado(
    val hospitalName: String,
    val date: String,
    val status: String,
    val proofImageUrl: String?,
    val observacao: String?,
    val timestamp: Long
) : Parcelable
