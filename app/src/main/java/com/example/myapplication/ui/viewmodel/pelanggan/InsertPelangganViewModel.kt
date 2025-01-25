package com.example.myapplication.ui.viewmodel.pelanggan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.Pelanggan
import com.example.myapplication.repository.PelangganRepository
import kotlinx.coroutines.launch

class InsertPelangganViewModel (
    private val pelangganRepository: PelangganRepository
): ViewModel(){
    var uiPelangganState by mutableStateOf(InsertPelangganUiState())
        private set
    fun updateInsertPelangganState(insertPelangganUiEvent: InsertPelangganUiEvent){
        uiPelangganState = InsertPelangganUiState(insertPelangganUiEvent = insertPelangganUiEvent)
    }
    fun insertPelanggan(){
        viewModelScope.launch {
            try {
                pelangganRepository.insertPelanggan(uiPelangganState.insertPelangganUiEvent.toPelanggan())
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}

data class InsertPelangganUiState(
    val insertPelangganUiEvent: InsertPelangganUiEvent = InsertPelangganUiEvent(),
)

data class InsertPelangganUiEvent(
    val id_pelanggan: Int = 0,
    val nama_pelanggan: String = "",
    val no_hp: String = "",
)

fun InsertPelangganUiEvent.toPelanggan(): Pelanggan = Pelanggan(
    id_pelanggan = id_pelanggan,
    nama_pelanggan = nama_pelanggan,
    no_hp = no_hp
)

fun Pelanggan.toUiStatePelanggan(): InsertPelangganUiState = InsertPelangganUiState(
    insertPelangganUiEvent = toInsertPelangganUiEvent()
)

fun Pelanggan.toInsertPelangganUiEvent(): InsertPelangganUiEvent = InsertPelangganUiEvent(
    id_pelanggan = id_pelanggan,
    nama_pelanggan = nama_pelanggan,
    no_hp = no_hp,
)