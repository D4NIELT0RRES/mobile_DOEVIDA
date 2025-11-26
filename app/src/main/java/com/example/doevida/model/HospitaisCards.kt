package com.example.doevida.model

import com.google.gson.annotations.SerializedName

data class HospitaisCards(
    val id: Int,

    // Casa tanto "nomeHospital" (seu JSON atual) quanto "nome" (se um dia vier assim)
    @SerializedName(value = "nomeHospital", alternate = ["nome"])
    val nomeHospital: String,

    val telefone: String,

    val cep: String? = null,
    val complemento: String? = null,

    @SerializedName("horario_abertura")
    val horario_abertura: String? = null,

    @SerializedName("horario_fechamento")
    val horario_fechamento: String? = null,

    val convenios: String? = null,

    @SerializedName("capacidade_maxima")
    val capacidade_maxima: Int? = null,

    val foto: String? = null,

    // O back agora envia "endereco"; mantemos opcional
    @SerializedName("endereco")
    private val enderecoApi: String? = null,

    // Coordenadas para o Mapa (Adicionado)
    val latitude: Double? = null,
    val longitude: Double? = null
) {
    val endereco: String
        get() = when {
            !enderecoApi.isNullOrBlank() -> enderecoApi
            !cep.isNullOrBlank() && !complemento.isNullOrBlank() -> "$cep - $complemento"
            !cep.isNullOrBlank() -> cep
            !complemento.isNullOrBlank() -> complemento
            else -> "Endereço não informado"
        }

    // Helper para retornar LatLng válido ou um padrão (São Paulo) se nulo
    // Útil para demonstração até o backend enviar dados reais
    fun getCoordenadas(): Pair<Double, Double> {
        return if (latitude != null && longitude != null) {
            latitude to longitude
        } else {
            // Mock temporário baseado no nome ou ID para espalhar no mapa (Demo)
            // Centro de SP: -23.5505, -46.6333
            when (id % 5) {
                0 -> -23.5565 to -46.6623 // Clínicas
                1 -> -23.5885 to -46.6452 // Hospital SP
                2 -> -23.5489 to -46.6388 // Santa Casa
                3 -> -23.5666 to -46.6522 // Outro
                else -> -23.5505 to -46.6333
            }
        }
    }
}
