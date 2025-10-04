package com.example.doevida.service

import com.example.doevida.model.Cadastro
import com.example.doevida.model.LoginRequest
import com.example.doevida.model.LoginResponse
import com.example.doevida.model.RecuperarSenhaRequest
import com.example.doevida.model.RecuperarSenhaResponse
import com.example.doevida.model.RedefinirSenhaRequest
import com.example.doevida.model.RespostaCadastro
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface UserService {

    @Headers("Content-Type: application/json")
    @POST("usuario")
    suspend fun insert(@Body user: Cadastro): Response<RespostaCadastro>

    @Headers("Content-Type: application/json")
    @POST("login")
    suspend fun login(@Body login: LoginRequest): Response<LoginResponse>

    @Headers("Content-Type: application/json")
    @POST("recuperar-senha")
    suspend fun recuperarSenha(@Body request: RecuperarSenhaRequest): Response<RecuperarSenhaResponse>
}