import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.Reservasi
import com.example.myapplication.model.Pelanggan
import com.example.myapplication.model.villa
import com.example.myapplication.repository.PelangganRepository
import com.example.myapplication.repository.ReservasiRepository
import com.example.myapplication.repository.VillaRepository
import kotlinx.coroutines.launch

class InsertReservasiViewModel(
    private val reservasiRepository: ReservasiRepository,
    val villaRepository: VillaRepository,
    private val pelangganRepository: PelangganRepository
) : ViewModel() {
    var uiReservasiState by mutableStateOf(InsertReservasiUiState())
        private set
    var villaNames by mutableStateOf<List<villa>>(emptyList())
        private set
    var pelangganNames by mutableStateOf<List<Pelanggan>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

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

    fun updateInsertReservasiState(insertReservasiUiEvent: InsertReservasiUiEvent) {
        uiReservasiState = InsertReservasiUiState(insertReservasiUiEvent = insertReservasiUiEvent)
    }

    suspend fun insertReservasi(navigateBack: () -> Unit) {
        viewModelScope.launch {
            try {
                val selectedVilla = villaNames.find { it.idVilla == uiReservasiState.insertReservasiUiEvent.idvilla }
                if (selectedVilla != null) {
                    val villaDetail = villaRepository.getVillaById(selectedVilla.idVilla)
                    val availableRooms = villaDetail.data.kamarTersedia
                    if (availableRooms >= uiReservasiState.insertReservasiUiEvent.jumlahKamar) {
                        reservasiRepository.insertReservasi(uiReservasiState.insertReservasiUiEvent.toReservasi())
                        uiReservasiState = uiReservasiState.copy(isSuccess = true)
                        navigateBack()
                    } else {
                        errorMessage = "Maaf, hanya tersedia $availableRooms kamar saat ini."
                    }
                } else {
                    errorMessage = "Villa tidak ditemukan."
                }
            } catch (e: Exception) {
                e.printStackTrace()
                errorMessage = "Terjadi kesalahan. Silakan coba lagi."
            } finally {
                isLoading = false
            }
        }
    }
}

data class InsertReservasiUiState(
    val insertReservasiUiEvent: InsertReservasiUiEvent = InsertReservasiUiEvent(),
    val isSuccess: Boolean = false
)

data class InsertReservasiUiEvent(
    val checkIn: String = "",
    val checkOut: String = "",
    val jumlahKamar: Int = 0,
    val idvilla: Int = 0,
    val idpelanggan: Int = 0
)

fun InsertReservasiUiEvent.toReservasi(): Reservasi = Reservasi(
    checkIn = checkIn,
    checkOut = checkOut,
    jumlahKamar = jumlahKamar,
    idVilla = idvilla,
    id_pelanggan = idpelanggan
)

fun Reservasi.toUiStateReservasi(): InsertReservasiUiState = InsertReservasiUiState(
    insertReservasiUiEvent = toInsertReservasiUiEvent()
)

fun Reservasi.toInsertReservasiUiEvent(): InsertReservasiUiEvent = InsertReservasiUiEvent(
    checkIn = checkIn,
    checkOut = checkOut,
    jumlahKamar = jumlahKamar,
    idvilla = idVilla,
    idpelanggan = id_pelanggan
)