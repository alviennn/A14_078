package com.example.myapplication.repository

import com.example.myapplication.model.VillaResponse
import com.example.myapplication.model.VillaResponseDetail
import com.example.myapplication.model.villa
import com.example.myapplication.service.VillaService
import java.io.IOException

interface VillaRepository{
    suspend fun getVilla(): VillaResponse
    suspend fun getVillaById(idVilla: Int): VillaResponseDetail
    suspend fun insertVilla(Villa: villa)
    suspend fun updateVilla(idVilla: Int, Villa: villa)
    suspend fun deleteVilla(idVilla: Int)
}

class NetworkVillaRepository(
    private val villaService: VillaService
): VillaRepository{
    override suspend fun insertVilla(Villa: villa) {
        villaService.insertVilla(Villa)
    }

    override suspend fun updateVilla(idVilla: Int, Villa: villa) {
        villaService.updateVilla(idVilla, Villa)
    }

    override suspend fun deleteVilla(idVilla: Int) {
        try {
            val response = villaService.deleteVilla(idVilla)
            if (!response.isSuccessful) {
                throw IOException(
                    "Failed to delete Villa. HTTP Status code: " +
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

    override suspend fun getVillaById(idVilla: Int): VillaResponseDetail {
        return villaService.getVillaById(idVilla)
    }

    override suspend fun getVilla(): VillaResponse {
        return try {
            villaService.getVilla()
        }catch (e: Exception){
            throw e
        }
    }
}