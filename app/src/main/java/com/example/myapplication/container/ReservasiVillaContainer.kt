package com.example.myapplication.container

import com.example.myapplication.repository.NetworkPelangganRepository
import com.example.myapplication.repository.NetworkReservasiRepository
import com.example.myapplication.repository.NetworkReviewRepository
import com.example.myapplication.repository.NetworkVillaRepository
import com.example.myapplication.repository.PelangganRepository
import com.example.myapplication.repository.ReservasiRepository
import com.example.myapplication.repository.ReviewRepository
import com.example.myapplication.repository.VillaRepository
import com.example.myapplication.service.PelangganService
import com.example.myapplication.service.ReservasiService
import com.example.myapplication.service.ReviewService
import com.example.myapplication.service.VillaService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer{
    val villaRepository: VillaRepository
    val pelangganRepository: PelangganRepository
    val reservasiRepository: ReservasiRepository
    val reviewRepository: ReviewRepository
}

class ReservasiVillaContainer : AppContainer{
    private val baseUrl = "http://10.0.2.2:3001/routes/"
    private val json = Json { ignoreUnknownKeys = true }
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    private val villaService : VillaService by lazy{
        retrofit.create(VillaService::class.java)
    }
    override val villaRepository: VillaRepository by lazy{
        NetworkVillaRepository(villaService)
    }
    private val pelangganService : PelangganService by lazy{
        retrofit.create(PelangganService::class.java)
    }
    override val pelangganRepository : PelangganRepository by lazy {
        NetworkPelangganRepository(pelangganService)
    }
    private val reservasiService : ReservasiService by lazy{
        retrofit.create(ReservasiService::class.java)
    }
    override val reservasiRepository : ReservasiRepository by lazy {
        NetworkReservasiRepository(reservasiService)
    }
    private val reviewService : ReviewService by lazy{
        retrofit.create(ReviewService::class.java)
    }
    override val reviewRepository : ReviewRepository by lazy {
        NetworkReviewRepository(reviewService)
    }
}