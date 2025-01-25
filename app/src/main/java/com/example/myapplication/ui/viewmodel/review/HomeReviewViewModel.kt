package com.example.myapplication.ui.viewmodel.review

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.Pelanggan
import com.example.myapplication.model.Reservasi
import com.example.myapplication.model.Review
import com.example.myapplication.model.villa
import com.example.myapplication.repository.PelangganRepository
import com.example.myapplication.repository.ReservasiRepository
import com.example.myapplication.repository.ReviewRepository
import com.example.myapplication.repository.VillaRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed class HomeReviewUiState{
    data class Success(
        val review: List<Review>,
        val reserverasi: List<Reservasi>,
        val pelanggan: List<Pelanggan>,
        val villa: List<villa>
    ) : HomeReviewUiState()
    object Error : HomeReviewUiState()
    object Loading : HomeReviewUiState()
}

class HomeReviewViewModel(
    private val reviewRepository: ReviewRepository,
    private val reservasiRepository: ReservasiRepository,
    private val pelannganRepository: PelangganRepository,
    private val villaRepository: VillaRepository
): ViewModel(){
    var reviewUiState: HomeReviewUiState by mutableStateOf(HomeReviewUiState.Loading)
        private set
    init {
        getReview()
    }

    fun getReview(){
        viewModelScope.launch {
            reviewUiState = HomeReviewUiState.Loading
            try {
                val review = reviewRepository.getReview().data
                val reservasi = reservasiRepository.getReservasi().data
                val pelanggan = pelannganRepository.getPelanggan().data
                val villa = villaRepository.getVilla().data
                reviewUiState = HomeReviewUiState.Success(review, reservasi, pelanggan, villa)
            }catch (e: IOException){
                reviewUiState = HomeReviewUiState.Error
            }catch (e: HttpException){
                reviewUiState = HomeReviewUiState.Error
            }
        }
    }
}