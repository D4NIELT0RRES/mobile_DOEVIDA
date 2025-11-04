package com.example.doevida.service

import android.content.Context
import com.example.doevida.util.TokenManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitFactory(private val context: Context) {

    private val BASE_URL = "http://10.0.2.2:8080/v1/doevida/"

    // Interceptor para adicionar o token de autorização
    private val authInterceptor = Interceptor { chain ->
        val token = TokenManager(context).getToken()
        val requestBuilder = chain.request().newBuilder()
        token?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }
        chain.proceed(requestBuilder.build())
    }

    // Interceptor para logar o corpo das requisições e respostas (MUITO útil para debug)
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor) // Adiciona o logging interceptor
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getUserService(): UserService {
        return retrofit.create(UserService::class.java)
    }

    fun getHospitalService(): HospitalService {
        return retrofit.create(HospitalService::class.java)
    }
}