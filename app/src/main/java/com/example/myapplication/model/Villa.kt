package com.example.myapplication.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class villa(
    @SerialName("id_villa")
    val idVilla: Int = 0,
    @SerialName("nama_villa")
    val namaVilla: String,
    val alamat: String,
    @SerialName("kamar_tersedia")
    val kamarTersedia: Int,
    val reviews : List<Review> = emptyList(),
    val Pelanggan : List<Pelanggan> = emptyList()
)

@Serializable
data class VillaResponse(
    val status: Boolean,
    val message: String,
    val data: List<villa>
)

@Serializable
data class VillaResponseDetail(
    val status: Boolean,
    val message: String,
    val data: villa
)
