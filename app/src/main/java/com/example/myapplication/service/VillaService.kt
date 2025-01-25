package com.example.myapplication.service

import com.example.myapplication.model.VillaResponse
import com.example.myapplication.model.VillaResponseDetail
import com.example.myapplication.model.villa
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface VillaService {
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @GET("villa")
    suspend fun getVilla(): VillaResponse
    @GET("villa/{idVilla}")
    suspend fun getVillaById(@Path("idVilla") idVilla: Int): VillaResponseDetail
    @POST("villa/store")
    suspend fun insertVilla(@Body Villa: villa)
    @PUT("villa/{idVilla}")
    suspend fun updateVilla(@Path("idVilla") idVilla: Int, @Body Villa: villa)
    @DELETE("villa/{idVilla}")
    suspend fun deleteVilla(@Path("idVilla") idVilla: Int): Response<Void>
}