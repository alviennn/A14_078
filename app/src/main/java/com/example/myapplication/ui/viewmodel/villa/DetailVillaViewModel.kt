package com.example.myapplication.ui.viewmodel.villa

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.Pelanggan
import com.example.myapplication.model.Review
import com.example.myapplication.model.villa
import com.example.myapplication.repository.VillaRepository
import com.example.myapplication.ui.view.DestinasiDetailVilla
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed class DetailVillaUiState {
    data class Success(
        val villa: villa,
        val reviews: List<Review>,
        val pelanggan: List<Pelanggan>
    ) : DetailVillaUiState()
    object Error : DetailVillaUiState()
    object Loading : DetailVillaUiState()
}

class DetailVillaViewModel(
    savedStateHandle: SavedStateHandle,
    private val villaRepository: VillaRepository
) : ViewModel() {
    var villaDetailState: DetailVillaUiState by mutableStateOf(DetailVillaUiState.Loading)
        private set

    private val _idvilla: Int = checkNotNull(savedStateHandle[ DestinasiDetailVilla.IDVILLA])

    init {
        getVillaById()
    }

    fun getVillaById() {
        viewModelScope.launch {
            villaDetailState = DetailVillaUiState.Loading
            villaDetailState = try {
                val villaResponse = villaRepository.getVillaById(_idvilla)
                val villa = villaResponse.data
                val reviews = villa.reviews
                val pelanggan = villa.Pelanggan
                DetailVillaUiState.Success(villa, reviews, pelanggan)
            } catch (e: IOException) {
                DetailVillaUiState.Error
            } catch (e: HttpException) {
                DetailVillaUiState.Error
            }
        }
    }
}
