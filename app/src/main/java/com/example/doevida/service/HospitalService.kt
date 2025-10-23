package com.example.doevida.service

import com.example.doevida.model.HospitalDetailResponse
import com.example.doevida.model.HospitalResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface HospitalService {
    @GET("hospital/mobile")
    suspend fun getHospitais(): Response<HospitalResponse>

    // Correção: Removido o "/mobile" para alinhar com a rota mais provável do backend
    @GET("hospital/{id}")
    suspend fun getHospitalById(@Path("id") id: Int): Response<HospitalDetailResponse>
}
