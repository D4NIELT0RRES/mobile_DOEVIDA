package com.example.doevida.service

import com.example.doevida.model.HospitalDetailResponse
import com.example.doevida.model.HospitalResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface HospitalService {
    @GET("hospital/mobile")
    suspend fun getHospitais(): Response<HospitalResponse>

    @GET("hospital/mobile/{id}")
    suspend fun getHospitalById(@Path("id") id: Int): Response<HospitalDetailResponse>
}