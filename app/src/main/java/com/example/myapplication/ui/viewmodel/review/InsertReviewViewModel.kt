package com.example.myapplication.ui.viewmodel.review

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.Reservasi
import com.example.myapplication.model.Review
import com.example.myapplication.repository.ReservasiRepository
import com.example.myapplication.repository.ReviewRepository
import kotlinx.coroutines.launch

class InsertReviewViewModel(
    private val reviewRepository: ReviewRepository,
    private val reservasiRepository: ReservasiRepository
) : ViewModel() {
    var uiReviewState by mutableStateOf(InsertReviewUiState())
        private set
    var reservasiList by mutableStateOf<List<Reservasi>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        loadReservasiList()
    }

    private fun loadReservasiList() {
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

    fun updateInsertReviewState(insertReviewUiEvent: InsertReviewUiEvent) {
        uiReviewState = uiReviewState.copy(insertReviewUiEvent = insertReviewUiEvent)
    }

    fun insertReview() {
        isLoading = true
        errorMessage = null
        viewModelScope.launch {
            try {
                reviewRepository.insertReview(uiReviewState.insertReviewUiEvent.toReview())
            } catch (e: Exception) {
                errorMessage = "Gagal memasukkan review: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}

data class InsertReviewUiState(
    val insertReviewUiEvent: InsertReviewUiEvent = InsertReviewUiEvent()
)

data class InsertReviewUiEvent(
    val id_reservasi: Int = 0,
    val nilai: String = "",
    val komentar: String = ""
)

fun InsertReviewUiEvent.toReview(): Review = Review(
    id_reservasi = id_reservasi,
    nilai = nilai,
    komentar = komentar
)

fun Review.toUiStateReview(): InsertReviewUiState = InsertReviewUiState(
    insertReviewUiEvent = toInsertReviewUiEvent()
)

fun Review.toInsertReviewUiEvent(): InsertReviewUiEvent = InsertReviewUiEvent(
    id_reservasi = id_reservasi,
    nilai = nilai,
    komentar = komentar
)
