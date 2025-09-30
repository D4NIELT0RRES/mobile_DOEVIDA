package com.example.doevida.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitFactory {

    private val BASE_URL = "http://10.107.144.11:8080/" //link da api

    private val retrofitFactory = Retrofit
        .Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    fun getUserService(): UserService {
        return retrofitFactory.create(UserService::class.java)
    }

    companion object

}