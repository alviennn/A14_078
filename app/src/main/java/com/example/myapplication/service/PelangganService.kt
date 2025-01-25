package com.example.myapplication.service

import com.example.myapplication.model.Pelanggan
import com.example.myapplication.model.PelangganResponse
import com.example.myapplication.model.PelangganResponseDetail
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PelangganService {
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @GET("pelanggan")
    suspend fun getPelanggan(): PelangganResponse
    @GET("pelanggan/{idPelanggan}")
    suspend fun getPelangganById(@Path("idPelanggan") idPelanggan: Int): PelangganResponseDetail
    @POST("pelanggan/store")
    suspend fun insertPelanggan(@Body pelanggan: Pelanggan)
    @PUT("pelanggan/{idPelanggan}")
    suspend fun updatePelanggan(@Path("idPelanggan") idPelanggan: Int, @Body pelanggan: Pelanggan)
    @DELETE("pelanggan/{idPelanggan}")
    suspend fun deletePelanggan(@Path("idPelanggan") idPelanggan: Int): Response<Void>
}