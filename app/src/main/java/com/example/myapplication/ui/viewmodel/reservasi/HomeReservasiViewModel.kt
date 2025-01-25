package com.example.myapplication.ui.viewmodel.reservasi

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.Reservasi
import com.example.myapplication.model.villa
import com.example.myapplication.repository.ReservasiRepository
import com.example.myapplication.repository.VillaRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed class HomeReservasiUiState{
    data class Success(
        val reservasi: List<Reservasi>,
        val villas: List<villa>,
    ): HomeReservasiUiState()
    object Error : HomeReservasiUiState()
    object Loading : HomeReservasiUiState()
}

class HomeReservasiViewModel(
    private val reservasiVillaRepository: ReservasiRepository,
    private val villaRepository: VillaRepository
): ViewModel() {
    var reservasiUiState: HomeReservasiUiState by mutableStateOf(HomeReservasiUiState.Loading)
        private set

    init {
        getReservasi()
    }

    fun getReservasi() {
        viewModelScope.launch {
            reservasiUiState = HomeReservasiUiState.Loading
            try {
                val reservasi = reservasiVillaRepository.getReservasi().data
                val villa = villaRepository.getVilla().data
                reservasiUiState = HomeReservasiUiState.Success(reservasi, villa)
            } catch (e: IOException) {
                reservasiUiState = HomeReservasiUiState.Error
            } catch (e: HttpException) {
                reservasiUiState = HomeReservasiUiState.Error
            }
        }
    }

    fun deleteReservasi(id_reservasi: Int) {
        viewModelScope.launch {
            try {
                reservasiVillaRepository.deleteReservasi(id_reservasi)
            } catch (e: IOException) {
                HomeReservasiUiState.Error
            } catch (e: IOException) {
                HomeReservasiUiState.Error
            }
        }
    }
}