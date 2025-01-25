package com.example.myapplication.ui.viewmodel.reservasi

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.Reservasi
import com.example.myapplication.model.Pelanggan
import com.example.myapplication.model.villa
import com.example.myapplication.repository.PelangganRepository
import com.example.myapplication.repository.ReservasiRepository
import com.example.myapplication.repository.VillaRepository
import com.example.myapplication.ui.view.reservasi.DestinasiDetailReservasi
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed class DetailReservasiUiState{
    data class Success(
        val reservasi: Reservasi,
        val villa: villa,
        val pelanggan: Pelanggan,
    ): DetailReservasiUiState()
    object Error : DetailReservasiUiState()
    object Loading : DetailReservasiUiState()
}

class DetailReservasiViewModel(
    savedStateHandle: SavedStateHandle,
    private val reservasiRepository: ReservasiRepository,
    private val villaRepository: VillaRepository,
    private val pelangganRepository: PelangganRepository
): ViewModel(){
    var detailReservasiState: DetailReservasiUiState by mutableStateOf(DetailReservasiUiState.Loading)
        private set
    private val _idreservasi: Int = checkNotNull(savedStateHandle[DestinasiDetailReservasi.IDRESERVASI])
    init {
        getReservasiById()
    }

    fun getReservasiById(){
        viewModelScope.launch {
            detailReservasiState = DetailReservasiUiState.Loading
            try {
                val reservasi = reservasiRepository.getReservasiById(_idreservasi).data
                val villa = villaRepository.getVillaById(reservasi.idVilla).data
                val pelanggan = pelangganRepository.getPelangganById(reservasi.id_pelanggan).data
                detailReservasiState = DetailReservasiUiState.Success(reservasi, villa, pelanggan)
            }catch (e: IOException) {
                DetailReservasiUiState.Error
            }catch (e: HttpException){
                DetailReservasiUiState.Error
            }
        }
    }

}
