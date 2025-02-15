package com.example.clienteflask

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url


interface APIservicio {
    @GET("demostracionesroboticas/miQR1")
    suspend fun funcion_registrarse(): Response<FlaskResponse>
}




