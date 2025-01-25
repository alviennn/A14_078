package com.example.myapplication.ui.viewmodel.villa

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.repository.VillaRepository
import com.example.myapplication.ui.view.DestinasiUpdateVilla
import kotlinx.coroutines.launch

class UpdateVillaViewModel (
    savedStateHandle: SavedStateHandle,
    private val villaRepository: VillaRepository
): ViewModel(){
    var updateVillaUiState by mutableStateOf(InsertVillaUiState())
        private set
    private val _idVilla: Int = checkNotNull(savedStateHandle[DestinasiUpdateVilla.IDVILLA])
    init {
        viewModelScope.launch {
            updateVillaUiState = villaRepository.getVillaById(_idVilla).data
                .toUiStateVilla()
        }
    }
    fun updateInsertVillaState(insertVillaUiEvent: InsertVillaUiEvent){
        updateVillaUiState = InsertVillaUiState(insertVillaUiEvent = insertVillaUiEvent)
    }

    fun updateVilla(){
        viewModelScope.launch {
            try {
                villaRepository.updateVilla(
                    _idVilla,
                    updateVillaUiState.insertVillaUiEvent.toVilla()
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}