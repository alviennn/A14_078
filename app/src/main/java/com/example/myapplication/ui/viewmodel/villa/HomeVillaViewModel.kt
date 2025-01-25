package com.example.myapplication.ui.viewmodel.villa

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.villa
import com.example.myapplication.repository.VillaRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed class HomeVillaUiState {
    data class Success(val villa: List<villa>) : HomeVillaUiState()
    object Error : HomeVillaUiState()
    object Loading : HomeVillaUiState()
}

class HomeVillaViewModel (
    private val villaRepository: VillaRepository
) : ViewModel() {
    var villaUiState: HomeVillaUiState by mutableStateOf(HomeVillaUiState.Loading)
        private set
    var isRefreshing by mutableStateOf(false)
        private set
    var snackbarMessage: String? by mutableStateOf(null)
        private set
    init {
        getVilla()
    }

    fun getVilla() {
        viewModelScope.launch {
            isRefreshing = true
            villaUiState = try {
                HomeVillaUiState.Success(villaRepository.getVilla().data)
            } catch (e: IOException) {
                snackbarMessage = "Network error: ${e.message}"
                HomeVillaUiState.Error
            } catch (e: HttpException) {
                snackbarMessage = "HTTP error: ${e.message}"
                HomeVillaUiState.Error
            } finally {
                isRefreshing = false
            }
        }
    }
    fun deleteVilla(idVilla: Int){
        viewModelScope.launch {
            try {
                villaRepository.deleteVilla(idVilla)
                getVilla()
            }catch (e: IOException){
                HomeVillaUiState.Error
            }catch (e: HttpException){
                HomeVillaUiState.Error
            }
        }
    }
}
