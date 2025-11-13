package com.example.doevida.model

// Modelo unificado para exibir tanto doações da API quanto manuais
data class CombinedDonationHistory(
    val hospitalName: String,
    val date: String, // Formato DD/MM/AAAA
    val status: String, // "Concluído", "Agendado", "Manual"
    val proofImageUrl: String? = null, // Apenas para doações manuais
    val timestamp: Long // Para ordenação
)
