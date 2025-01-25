package com.example.myapplication.ui.viewmodel.villa

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.villa
import com.example.myapplication.repository.VillaRepository
import kotlinx.coroutines.launch

class InsertVillaViewModel(
    private val villaRepository: VillaRepository
): ViewModel (){
    var uiVillaState by mutableStateOf(InsertVillaUiState())
        private set
    fun updateInsertVillaState(insertVillaUiEvent: InsertVillaUiEvent){
        uiVillaState = InsertVillaUiState(insertVillaUiEvent = insertVillaUiEvent)
    }
    fun insertVilla(){
        viewModelScope.launch {
            try {
                villaRepository.insertVilla(uiVillaState.insertVillaUiEvent.toVilla())
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}

data class InsertVillaUiState(
    val insertVillaUiEvent: InsertVillaUiEvent = InsertVillaUiEvent(),
)

data class InsertVillaUiEvent(
    val namaVilla: String = "",
    val alamat: String = "",
    val kamarTersedia: Int = 0,
)

fun InsertVillaUiEvent.toVilla(): villa = villa(
    namaVilla = namaVilla,
    alamat = alamat,
    kamarTersedia = kamarTersedia
)

fun villa.toUiStateVilla(): InsertVillaUiState = InsertVillaUiState(
    insertVillaUiEvent = toInsertVillaUiEvent()
)

fun villa.toInsertVillaUiEvent(): InsertVillaUiEvent = InsertVillaUiEvent(
    namaVilla = namaVilla,
    alamat = alamat,
    kamarTersedia = kamarTersedia,
)