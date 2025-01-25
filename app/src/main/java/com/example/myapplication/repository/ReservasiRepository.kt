package com.example.myapplication.repository

import com.example.myapplication.model.Reservasi
import com.example.myapplication.model.ReservasiResponse
import com.example.myapplication.model.ReservasiResponseDetail
import com.example.myapplication.service.ReservasiService
import java.io.IOException

interface ReservasiRepository{
    suspend fun getReservasi(): ReservasiResponse
    suspend fun getReservasiById(idReservasi: Int): ReservasiResponseDetail
    suspend fun insertReservasi(Reservasi: Reservasi)
    suspend fun updateReservasi(idReservasi: Int, Reservasi: Reservasi)
    suspend fun deleteReservasi(idReservasi: Int)
}

class NetworkReservasiRepository(
    private val ReservasiService: ReservasiService
): ReservasiRepository{
    override suspend fun insertReservasi(Reservasi: Reservasi) {
        ReservasiService.insertReservasi(Reservasi)
    }
    override suspend fun updateReservasi(idReservasi: Int, Reservasi: Reservasi) {
        ReservasiService.updateReservasi(idReservasi, Reservasi)
    }
    override suspend fun deleteReservasi(idReservasi: Int){
        try {
            val response = ReservasiService.deleteReservasi(idReservasi)
            if (!response.isSuccessful) {
                throw IOException(
                    "Failed to delete Reservasi. HTTP Status code: " +
                            "${response.code()}"
                )
                } else {
                    response.message()
                    println(response.code())
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getReservasiById(idReservasi: Int): ReservasiResponseDetail {
        return ReservasiService.getReservasiById(idReservasi)
    }

    override suspend fun getReservasi(): ReservasiResponse {
        return try {
            ReservasiService.getReservasi()
        }catch (e: Exception){
            throw e
        }
    }
}