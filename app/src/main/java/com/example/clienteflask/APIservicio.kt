package com.example.clienteflask

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Url
import retrofit2.http.POST


interface APIservicio {
    @GET
    suspend fun funcion_registrarse(@Url url: String): Response<FlaskResponse>


    @POST
    suspend fun funcion_aceptarRobot(
        @Url url: String,
        @Body peticionBody: PeticionBody,
        @Header("X-CSRF-Token") miParametroCSRFtoken: String
    ): Response<FlaskResponse>

}












