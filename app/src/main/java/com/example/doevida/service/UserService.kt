package com.example.doevida.service

import com.example.doevida.model.AgendamentoRequest
import com.example.doevida.model.Cadastro
import com.example.doevida.model.ComplementoPerfilRequest
import com.example.doevida.model.ComplementoPerfilResponse
import com.example.doevida.model.HistoricoResponse
import com.example.doevida.model.LoginRequest
import com.example.doevida.model.LoginResponse
import com.example.doevida.model.RecuperarSenhaRequest
import com.example.doevida.model.RecuperarSenhaResponse
import com.example.doevida.model.RedefinirSenhaRequest
import com.example.doevida.model.RedefinirSenhaResponse
import com.example.doevida.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserService {
    @Headers("Content-Type: application/json")
    @POST("usuarios")
    suspend fun insert(@Body cadastro: Cadastro): Response<Cadastro>

    @Headers("Content-Type: application/json")
    @POST("usuarios/login")
    suspend fun login(@Body login: LoginRequest): Response<LoginResponse>

    @Headers("Content-Type: application/json")
    @PATCH("usuarios/me/complemento")
    suspend fun completarPerfil(@Body complemento: ComplementoPerfilRequest): Response<ComplementoPerfilResponse>

    @Headers("Content-Type: application/json")
    @POST("agendamentos")
    suspend fun agendarDoacao(@Body agendamento: AgendamentoRequest): Response<Unit>

    @Headers("Content-Type: application/json")
    @POST("usuarios/recuperar-senha") // A rota correta para a recuperação
    suspend fun recuperarSenha(@Body request: RecuperarSenhaRequest): Response<RecuperarSenhaResponse>

    @Headers("Content-Type: application/json")
    @POST("usuarios/redefinir-senha")
    suspend fun redefinirSenha(@Body request: RedefinirSenhaRequest): Response<RedefinirSenhaResponse>

    @Headers("Content-Type: application/json")
    @GET("usuarios/{id}/agendamentos")
    suspend fun getHistorico(@Path("id") id: Int): Response<HistoricoResponse>

    @Headers("Content-Type: application/json")
    @GET("usuarios/{id}")
    suspend fun getPerfil(@Path("id") id: Int): Response<User>

    @Headers("Content-Type: application/json")
    @PUT("usuarios/{id}")
    suspend fun updatePerfil(@Path("id") id: Int, @Body user: User): Response<User>
}
