package com.example.myapplication.service

import com.example.myapplication.model.Review
import com.example.myapplication.model.ReviewResponse
import com.example.myapplication.model.ReviewResponseDetail
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ReviewService {
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @GET("review")
    suspend fun getReview(): ReviewResponse
    @GET("review/{idReview}")
    suspend fun getReviewById(@Path("idReview") idReview: Int): ReviewResponseDetail
    @POST("review/store")
    suspend fun insertReview(@Body Review: Review)
    @PUT("review/{idReview}")
    suspend fun updateReview(@Path("idReview") idReview: Int, @Body Review: Review)
    @DELETE("review/{idReview}")
    suspend fun deleteReview(@Path("idReview") idReview: Int): Response<Void>
}