package com.example.myapplication.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Review(
    @SerialName("id_review")
    val idReview: Int = 0,
    val id_reservasi: Int = 0,
    val id_pelanggan: Int = 0,
    val idVilla: Int = 0,
    val nilai: String,
    val komentar: String
)

@Serializable
data class ReviewResponse(
    val status: Boolean,
    val message: String,
    val data: List<Review>
)

@Serializable
data class ReviewResponseDetail(
    val status: Boolean,
    val message: String,
    val data: Review
)