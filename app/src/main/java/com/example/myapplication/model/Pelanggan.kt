package com.example.myapplication.model

import kotlinx.serialization.Serializable

@Serializable
data class Pelanggan(
    val id_pelanggan: Int,
    val nama_pelanggan: String,
    val no_hp: String,
    val reviews : List<Review> = emptyList()
)

@Serializable
data class PelangganResponse(
    val status:Boolean,
    val message: String,
    val data: List<Pelanggan>
)

@Serializable
data class PelangganResponseDetail(
    val status:Boolean,
    val message: String,
    val data: Pelanggan
)