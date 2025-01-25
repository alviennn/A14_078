package com.example.myapplication.repository

import com.example.myapplication.model.Review
import com.example.myapplication.model.ReviewResponse
import com.example.myapplication.model.ReviewResponseDetail
import com.example.myapplication.service.ReviewService
import okio.IOException

interface ReviewRepository{
    suspend fun getReview(): ReviewResponse
    suspend fun getReviewById(idReview: Int): ReviewResponseDetail
    suspend fun insertReview(Review: Review)
    suspend fun updateReview(idReview: Int, Review: Review)
    suspend fun deleteReview(idReview: Int)
}

class NetworkReviewRepository(
    private val reviewService: ReviewService
): ReviewRepository{
    override suspend fun insertReview(Review: Review) {
        reviewService.insertReview(Review)
    }
    override suspend fun updateReview(idReview: Int, Review: Review) {
        reviewService.updateReview(idReview, Review)
    }
    override suspend fun deleteReview(idReview: Int) {
        try {
            val response = reviewService.deleteReview(idReview)
            if (!response.isSuccessful) {
                throw IOException(
                    "Failed to delete Review. HTTP Status code: " +
                            "${response.code()}"
                )
            } else {
                response.message()
                println(response.code())
            }
        }catch (e: Exception){
            throw e
        }
    }
    override suspend fun getReviewById(idReview: Int): ReviewResponseDetail {
        return reviewService.getReviewById(idReview)
    }
    override suspend fun getReview(): ReviewResponse {
        return try {
            reviewService.getReview()
        } catch (E: Exception) {
            throw E
        }
    }
}

