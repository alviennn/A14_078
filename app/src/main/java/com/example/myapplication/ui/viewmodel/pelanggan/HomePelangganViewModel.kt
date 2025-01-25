package com.example.myapplication.ui.viewmodel.pelanggan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.Pelanggan
import com.example.myapplication.repository.PelangganRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed class HomePelangganUiState{
    data class Success(val pelanggan: List<Pelanggan>) : HomePelangganUiState()
    object Error : HomePelangganUiState()
    object Loading : HomePelangganUiState()
}

class HomePelangganViewModel(
    private val pelangganRepository: PelangganRepository
): ViewModel(){
    var pelangganUiState: HomePelangganUiState by mutableStateOf(HomePelangganUiState.Loading)
        private set
    var isRefreshing by mutableStateOf(false)
        private set
    var snackbarMessage: String? by mutableStateOf(null)
        private set
    init {
        getPelanggan()
    }

    fun getPelanggan(){
        viewModelScope.launch {
            isRefreshing = true
            pelangganUiState = try {
                HomePelangganUiState.Success(pelangganRepository.getPelanggan().data)
            }catch (e: IOException){
                snackbarMessage = "Network error: ${e.message}"
                HomePelangganUiState.Error
            }catch (e: HttpException){
                snackbarMessage = "HTTP error: ${e.message}"
                HomePelangganUiState.Error
            } finally {
                isRefreshing = false
            }
        }
    }
    fun deletePelanggan(idPelanggan: Int){
        viewModelScope.launch {
            try {
                pelangganRepository.deletePelanggan(idPelanggan)
                getPelanggan()
            } catch (e: IOException) {
                HomePelangganUiState.Error
            } catch (e: HttpException) {
                HomePelangganUiState.Error
            }
        }
    }
}
