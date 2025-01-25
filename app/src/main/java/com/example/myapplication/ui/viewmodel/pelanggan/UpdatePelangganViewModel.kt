package com.example.myapplication.ui.viewmodel.pelanggan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.Pelanggan
import com.example.myapplication.repository.PelangganRepository
import com.example.myapplication.ui.view.pelanggan.DestinasiUpdatePelanggan
import kotlinx.coroutines.launch

class UpdatePelangganViewModel(
    savedStateHandle: SavedStateHandle,
    private val pelangganRepository: PelangganRepository
) : ViewModel() {
    var uiPelangganState by mutableStateOf(InsertPelangganUiState())
        private set
    private val _idPelanggan: Int = checkNotNull(savedStateHandle[DestinasiUpdatePelanggan.IDPELANGGAN])

    init {
        viewModelScope.launch {
            try {
                val pelanggan = pelangganRepository.getPelangganById(_idPelanggan).data
                uiPelangganState = pelanggan.toUiStatePelanggan()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateInsertPelangganState(insertPelangganUiEvent: InsertPelangganUiEvent) {
        uiPelangganState = InsertPelangganUiState(insertPelangganUiEvent = insertPelangganUiEvent)
    }

    fun updatePelanggan() {
        viewModelScope.launch {
            try {
                pelangganRepository.updatePelanggan(
                    _idPelanggan,
                    uiPelangganState.insertPelangganUiEvent.toPelanggan()
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}