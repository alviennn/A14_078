package com.example.myapplication.service

import com.example.myapplication.model.Reservasi
import com.example.myapplication.model.ReservasiResponse
import com.example.myapplication.model.ReservasiResponseDetail
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ReservasiService {
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @GET("reservasi")
    suspend fun getReservasi(): ReservasiResponse
    @GET("reservasi/{idReservasi}")
    suspend fun getReservasiById(@Path("idReservasi") idReservasi: Int): ReservasiResponseDetail
    @POST("reservasi/store")
    suspend fun insertReservasi(@Body Reservasi: Reservasi)
    @PUT("reservasi/{idReservasi}")
    suspend fun updateReservasi(@Path("idReservasi") idReservasi: Int, @Body Reservasi: Reservasi)
    @DELETE("reservasi/{idReservasi}")
    suspend fun deleteReservasi(@Path("idReservasi") idReservasi: Int): Response<Void>
}