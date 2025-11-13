package com.example.doevida.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DoacaoManual(
    val userId: Int, // Adicionado para associar a doação a um usuário
    val hospitalName: String,
    val donationDate: String,
    val proofImageUrl: String,
    val observacao: String?,
    val timestamp: Long = System.currentTimeMillis()
) : Parcelable
