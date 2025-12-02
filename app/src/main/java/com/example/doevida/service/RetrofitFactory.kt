package com.example.doevida.service

import android.content.Context
import com.example.doevida.util.TokenManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitFactory(private val context: Context) {

    private val BASE_URL = "http://10.0.2.2:8080/v1/doevida/"

    // Interceptor inteligente que só adiciona o token em rotas privadas
    private val authInterceptor = Interceptor { chain ->
        val request = chain.request()
        val requestBuilder = request.newBuilder()

        // Verifica se a rota NÃO é pública
        if (!request.url.pathSegments.contains("login") && !request.url.pathSegments.contains("usuario")) {
            val token = TokenManager(context).getToken()
            if (token != null) {
                requestBuilder.addHeader("Authorization", "Bearer $token")
            }
        }
        
        chain.proceed(requestBuilder.build())
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .connectTimeout(60, TimeUnit.SECONDS) // Aumentado para 60s
        .readTimeout(60, TimeUnit.SECONDS)    // Aumentado para 60s
        .writeTimeout(60, TimeUnit.SECONDS)   // Aumentado para 60s
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
