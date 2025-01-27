package com.example.myapplication.ui.view

import CustomTopAppBar
import InsertReservasiUiEvent
import InsertReservasiUiState
import InsertReservasiViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.model.Pelanggan
import com.example.myapplication.model.villa
import com.example.myapplication.ui.customwidget.DatePickerField
import com.example.myapplication.ui.customwidget.DropdownSelector
import com.example.myapplication.ui.navigation.DestinasiNavigasi
import com.example.myapplication.ui.viewmodel.PenyediaViewModel
import kotlinx.coroutines.launch

object DestinasiInsertReservasi : DestinasiNavigasi {
    override val route = "insert_reservasi"
    override val titleRes = "Insert Reservasi"
    val descriptionRes = "Masukkan data Reservasi"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertReservasiView(
    navigateBack: () -> Unit,
    viewModel: InsertReservasiViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    LaunchedEffect(Unit) {
        viewModel.loadInitialData()
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CustomTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = DestinasiInsertReservasi.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                description = DestinasiInsertReservasi.descriptionRes,
                navigateUp = navigateBack
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        EntryReservasiBody(
            insertReservasiUiState = viewModel.uiReservasiState,
            villaList = viewModel.villaNames,
            pelangganList = viewModel.pelangganNames,
            onValueChange = viewModel::updateInsertReservasiState,
            onSaveClick = {
                coroutineScope.launch {
                    val selectedVilla = viewModel.villaNames.find { it.idVilla == viewModel.uiReservasiState.insertReservasiUiEvent.idvilla }
                    if (selectedVilla != null) {
                        val villaDetail = viewModel.villaRepository.getVillaById(selectedVilla.idVilla)
                        val availableRooms = villaDetail.data.kamarTersedia
                        if (availableRooms >= viewModel.uiReservasiState.insertReservasiUiEvent.jumlahKamar) {
                            viewModel.insertReservasi(navigateBack)
                            snackbarHostState.showSnackbar("Data reservasi berhasil disimpan!")
                        } else {
                            snackbarHostState.showSnackbar("Kamar Tidak Tersedia. Hanya $availableRooms kamar tersisa.")
                        }
                    } else {
                        snackbarHostState.showSnackbar("Silakan pilih villa yang valid.")
                    }
                }
            },
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )
    }
}

@Composable
fun EntryReservasiBody(
    insertReservasiUiState: InsertReservasiUiState,
    villaList: List<villa>,
    pelangganList: List<Pelanggan>,
    onValueChange: InsertReservasiUiEvent.() -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ReservasiForm(
            uiEvent = insertReservasiUiState.insertReservasiUiEvent,
            villaList = villaList,
            pelangganList = pelangganList,
            onValueChange = onValueChange
        )
        Button(
            onClick = onSaveClick,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2196F3),
                contentColor = MaterialTheme.colorScheme.onError
            )
        ) {
            Text(text = "Simpan")
        }
    }
}

@Composable
fun ReservasiForm(
    uiEvent: InsertReservasiUiEvent,
    villaList: List<villa>,
    pelangganList: List<Pelanggan>,
    onValueChange: InsertReservasiUiEvent.() -> Unit
) {
    Column(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        var checkInDate by remember { mutableStateOf(uiEvent.checkIn) }
        DatePickerField(
            label = "Tanggal Check-In",
            selectedDate = checkInDate,
            onDateSelected = {
                checkInDate = it
                uiEvent.copy(checkIn = it).onValueChange()
            }
        )
        var checkOutDate by remember { mutableStateOf(uiEvent.checkOut) }
        DatePickerField(
            label = "Tanggal Check-Out",
            selectedDate = checkOutDate,
            onDateSelected = {
                checkOutDate = it
                uiEvent.copy(checkOut = it).onValueChange()
            }
        )

        OutlinedTextField(
            value = uiEvent.jumlahKamar.toString(),
            onValueChange = { uiEvent.copy(jumlahKamar = it.toIntOrNull() ?: 0).onValueChange() },
            label = { Text("Jumlah Kamar") },
            modifier = Modifier.fillMaxWidth()
        )

        DropdownSelector(
            label = "Pilih Villa",
            options = villaList.map { it.namaVilla },
            selectedOption = villaList.find { it.idVilla == uiEvent.idvilla }?.namaVilla.orEmpty(),
            onOptionSelected = { selected ->
                val selectedVilla = villaList.find { it.namaVilla == selected }
                uiEvent.copy(idvilla = selectedVilla?.idVilla ?: 0).onValueChange()
            }
        )

        DropdownSelector(
            label = "Pilih Pelanggan",
            options = pelangganList.map { it.nama_pelanggan },
            selectedOption = pelangganList.find { it.id_pelanggan == uiEvent.idpelanggan }?.nama_pelanggan.orEmpty(),
            onOptionSelected = { selected ->
                val selectedPelanggan = pelangganList.find { it.nama_pelanggan == selected }
                uiEvent.copy(idpelanggan = selectedPelanggan?.id_pelanggan ?: 0).onValueChange()
            }
        )
    }
}