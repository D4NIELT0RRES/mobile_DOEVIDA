package com.example.doevida.service

import com.example.doevida.model.AgendamentoRequest
import com.example.doevida.model.Cadastro
import com.example.doevida.model.ComplementoPerfilRequest
import com.example.doevida.model.ComplementoPerfilResponse
import com.example.doevida.model.LoginRequest
import com.example.doevida.model.LoginResponse
import com.example.doevida.model.RecuperarSenhaRequest
import com.example.doevida.model.RecuperarSenhaResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST

interface UserService {
    @POST("/usuarios")
    suspend fun insert(@Body cadastro: Cadastro): Response<Cadastro>

    @POST("/usuarios/login")
    suspend fun login(@Body login: LoginRequest): Response<LoginResponse>

    @PATCH("/usuarios/me/complemento")
    suspend fun completarPerfil(@Body complemento: ComplementoPerfilRequest): Response<ComplementoPerfilResponse>

    @POST("/agendamentos")
    suspend fun agendarDoacao(@Body agendamento: AgendamentoRequest): Response<Unit>

    @POST("/usuarios/recuperar-senha") // A rota correta para a recuperação
    suspend fun recuperarSenha(@Body request: RecuperarSenhaRequest): Response<RecuperarSenhaResponse>
}
