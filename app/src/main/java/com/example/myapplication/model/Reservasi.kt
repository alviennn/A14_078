package com.example.myapplication.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Reservasi(
    @SerialName("id_reservasi")
    val idReservasi: Int = 0,
    @SerialName("id_villa")
    val idVilla: Int = 0,
    val id_pelanggan: Int = 0,
    @SerialName("check_in")
    val checkIn: String,
    @SerialName("check_out")
    val checkOut: String,
    @SerialName("jumlah_kamar")
    val jumlahKamar: Int,
)

@Serializable
data class ReservasiResponse(
    val status: Boolean,
    val message: String,
    val data: List<Reservasi>
)

@Serializable
data class ReservasiResponseDetail(
    val status: Boolean,
    val message: String,
    val data: Reservasi
)