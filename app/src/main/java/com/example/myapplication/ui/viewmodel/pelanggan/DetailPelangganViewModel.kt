package com.example.myapplication.ui.viewmodel.pelanggan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.Pelanggan
import com.example.myapplication.repository.PelangganRepository
import com.example.myapplication.ui.view.pelanggan.DestinasiDetailPelanggan
import com.example.myapplication.ui.viewmodel.villa.DetailVillaUiState
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed class DetailPelangganUiState{
    data class Success(val pelanggan: Pelanggan): DetailPelangganUiState()
    object Error : DetailPelangganUiState()
    object Loading : DetailPelangganUiState()
}

class DetailPelangganViewModel(
    savedStateHandle: SavedStateHandle,
    private val pelangganRepository: PelangganRepository
) : ViewModel(){
    var pelangganDetailState: DetailPelangganUiState by mutableStateOf(DetailPelangganUiState.Loading)
        private set
    private val _idPelanggan: Int = checkNotNull(savedStateHandle[DestinasiDetailPelanggan.IDPELANGGAN])

    init {
        getPelangganById()
    }

    fun getPelangganById(){
        viewModelScope.launch {
            pelangganDetailState = DetailPelangganUiState.Loading
            pelangganDetailState = try {
                val pelangganResponse = pelangganRepository.getPelangganById(_idPelanggan)
                val pelanggan = pelangganResponse.data
                DetailPelangganUiState.Success(pelanggan)
            } catch (e: IOException) {
                DetailPelangganUiState.Error
            } catch (e: HttpException) {
                DetailPelangganUiState.Error
            }
        }
    }
}