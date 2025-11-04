package com.example.doevida.service

import com.example.doevida.model.HospitalDetailResponse
import com.example.doevida.model.HospitalResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface HospitalService {
    @Headers("Content-Type: application/json")
    @GET("hospital/mobile")
    suspend fun getHospitais(): Response<HospitalResponse>


    @Headers("Content-Type: application/json")
    @GET("hospital/{id}")
    suspend fun getHospitalById(@Path("id") id: Int): Response<HospitalDetailResponse>
}
