package com.example.doevida.service

import com.example.doevida.model.HospitalDetailResponse
import com.example.doevida.model.HospitalResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

class RetrofitFactory {

    private val BASE_URL = "http://10.107.144.2:8080/v1/doevida/"

    // Cria um interceptor para logar as requisições e respostas
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Cria um cliente OkHttp e adiciona o interceptor
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client) // Adiciona o cliente OkHttp ao Retrofit
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getUserService(): UserService {
        return retrofit.create(UserService::class.java)
    }

    fun getHospitalService(): HospitalService {
        return retrofit.create(HospitalService::class.java)
    }
}

