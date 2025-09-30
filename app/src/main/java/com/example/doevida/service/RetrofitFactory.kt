package com.example.doevida.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitFactory {

    private val BASE_URL = "http://10.107.144.16:8080/v1/doevida/" // link da API

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getUserService(): UserService {
        return retrofit.create(UserService::class.java)
    }
}
