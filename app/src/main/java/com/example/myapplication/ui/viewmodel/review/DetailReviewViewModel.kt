package com.example.myapplication.ui.viewmodel.review

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
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
import com.example.myapplication.ui.view.review.DestinasiDetailReview
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed class DetailReviewUiState{
    data class Success(
        val review: Review,
        val reservasi: Reservasi,
    ): DetailReviewUiState()
    object Error : DetailReviewUiState()
    object Loading : DetailReviewUiState()
}

class DetailReviewViewModel (
    savedStateHandle: SavedStateHandle,
    private val reviewRepository: ReviewRepository,
    private val reservasiRepository: ReservasiRepository
): ViewModel(){
    var detailReviewState by mutableStateOf(InsertReviewUiState())
        private set
    var reservasiList by mutableStateOf<List<Reservasi>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set
    private val _idReview: Int = checkNotNull(savedStateHandle[DestinasiDetailReview.IDREVIEW])
    init {
        viewModelScope.launch {
            detailReviewState = reviewRepository.getReviewById(_idReview).data
                .toUiStateReview()
        }
    }
    fun loadReservasiList() {
        isLoading = true
        errorMessage = null
        viewModelScope.launch {
            try {
                val reservasiResponse = reservasiRepository.getReservasi()
                reservasiList = reservasiResponse.data
            } catch (e: Exception) {
                errorMessage = "Gagal memuat data reservasi: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
    fun updateInsertReviewState(insertReviewUiEvent: InsertReviewUiEvent){
        detailReviewState = InsertReviewUiState(insertReviewUiEvent = insertReviewUiEvent)
    }
    fun updateReview(){
        viewModelScope.launch {
            try {
                reviewRepository.updateReview(
                    _idReview,
                    detailReviewState.insertReviewUiEvent.toReview()
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}