package com.example.doevida.service

import com.example.doevida.model.Cadastro
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface UserService {

    @Headers("Content-Type: application/json")
    @POST("/v1/doevida/usuario")
    fun insert(@Body user: Cadastro): Call<Cadastro>

}