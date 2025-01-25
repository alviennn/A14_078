package com.example.myapplication.ui.viewmodel.reservasi

import InsertReservasiUiEvent
import InsertReservasiUiState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.Pelanggan
import com.example.myapplication.model.villa
import com.example.myapplication.repository.PelangganRepository
import com.example.myapplication.repository.ReservasiRepository
import com.example.myapplication.repository.VillaRepository
import com.example.myapplication.ui.view.reservasi.DestinasiUpdateReservasi
import kotlinx.coroutines.launch
import toReservasi
import toUiStateReservasi

class UpdateReservasiViewModel (
    savedStateHandle: SavedStateHandle,
    private val reservasiRepository: ReservasiRepository,
    private val villaRepository: VillaRepository,
    private val pelangganRepository: PelangganRepository
): ViewModel(){
    var updateReservasiUiState by mutableStateOf(InsertReservasiUiState())
        private set
    var villaNames by mutableStateOf<List<villa>>(emptyList())
        private set
    var pelangganNames by mutableStateOf<List<Pelanggan>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set
    private val _idReservasi: Int = checkNotNull(savedStateHandle[DestinasiUpdateReservasi.IDRESERVASI])
    init {
        viewModelScope.launch {
            updateReservasiUiState = reservasiRepository.getReservasiById(_idReservasi).data
                .toUiStateReservasi()
        }
    }

    fun loadInitialData() {
        isLoading = true
        errorMessage = null
        viewModelScope.launch {
            try {
                val villaResponse = villaRepository.getVilla()
                villaNames = villaResponse.data
                val pelangganResponse = pelangganRepository.getPelanggan()
                pelangganNames = pelangganResponse.data
            } catch (e: Exception) {
                errorMessage = "Gagal memuat data: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun updateInsertReservasiState(insertReservasiUiEvent: InsertReservasiUiEvent){
        updateReservasiUiState = InsertReservasiUiState(insertReservasiUiEvent = insertReservasiUiEvent)
    }
    fun updateReservasi(){
        viewModelScope.launch {
            try {
                reservasiRepository.updateReservasi(
                    _idReservasi,
                    updateReservasiUiState.insertReservasiUiEvent.toReservasi()
                )
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}