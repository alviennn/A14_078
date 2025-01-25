package com.example.myapplication.repository

import com.example.myapplication.model.Pelanggan
import com.example.myapplication.model.PelangganResponse
import com.example.myapplication.model.PelangganResponseDetail
import com.example.myapplication.service.PelangganService
import java.io.IOException

interface PelangganRepository{
    suspend fun getPelanggan(): PelangganResponse
    suspend fun getPelangganById(idPelanggan: Int): PelangganResponseDetail
    suspend fun insertPelanggan(Pelanggan: Pelanggan)
    suspend fun updatePelanggan(idPelanggan: Int,Pelanggan: Pelanggan)
    suspend fun deletePelanggan(idPelanggan: Int)
}

class NetworkPelangganRepository(
    private val pelangganService: PelangganService
): PelangganRepository {
    override suspend fun insertPelanggan(Pelanggan: Pelanggan) {
        pelangganService.insertPelanggan(Pelanggan)
    }

    override suspend fun updatePelanggan(idPelanggan: Int, Pelanggan: Pelanggan) {
        pelangganService.updatePelanggan(idPelanggan, Pelanggan)
    }

    override suspend fun deletePelanggan(idPelanggan: Int) {
        try {
            val response = pelangganService.deletePelanggan(idPelanggan)
            if (!response.isSuccessful) {
                throw IOException(
                    "Failed to delete pelanggan. HTTP Status code: " +
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
    override suspend fun getPelangganById(idPelanggan: Int): PelangganResponseDetail {
        return pelangganService.getPelangganById(idPelanggan)
    }
    override suspend fun getPelanggan(): PelangganResponse {
        return try {
            pelangganService.getPelanggan()
        }catch (e: Exception){
            throw e
        }
    }
}