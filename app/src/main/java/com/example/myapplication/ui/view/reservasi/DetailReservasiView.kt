package com.example.myapplication.ui.view.reservasi

import CustomTopAppBar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.ui.navigation.DestinasiNavigasi
import com.example.myapplication.ui.viewmodel.PenyediaViewModel
import com.example.myapplication.ui.viewmodel.reservasi.DetailReservasiUiState
import com.example.myapplication.ui.viewmodel.reservasi.DetailReservasiViewModel

object DestinasiDetailReservasi : DestinasiNavigasi {
    override val route = "reservasi_detail"
    override val titleRes = "Detail Reservasi"
    val descriptionRes = "Deskripsi Reservasi"
    const val IDRESERVASI = "idReservasi"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailReservasiScreen(
    onBackClick: () -> Unit,
    onEditClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailReservasiViewModel = viewModel(factory = PenyediaViewModel.Factory)
    ) {
    val uiState = viewModel.detailReservasiState

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = DestinasiDetailReservasi.titleRes,
                canNavigateBack = true,
                navigateUp = onBackClick,
                description = DestinasiDetailReservasi.descriptionRes
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (uiState) {
                    is DetailReservasiUiState.Loading -> CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )

                    is DetailReservasiUiState.Error -> Text(
                        text = "Gagal Memuat Data Reservasi.",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.align(Alignment.Center)
                    )

                    is DetailReservasiUiState.Success -> {
                        val reservasi = uiState.reservasi
                        val villa = uiState.villa
                        val pelanggan = uiState.pelanggan

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                        ) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                elevation = CardDefaults.cardElevation(8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFE3F2FD)
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Text(
                                        text = "Detail Reservasi",
                                        style = MaterialTheme.typography.titleMedium,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )
                                    Text(
                                        text = "Nama Villa: ${villa.namaVilla}",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Text(
                                        text = "Nama Pelanggan: ${pelanggan.nama_pelanggan}",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Text(
                                        text = "Jumlah Kamar: ${reservasi.jumlahKamar}",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Text(
                                        text = "Tanggal Check In: ${reservasi.checkIn}",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Text(
                                        text = "Tanggal Check Out: ${reservasi.checkOut}",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Button(
                                    onClick = { onEditClick(reservasi.idReservasi) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF2196F3),
                                        contentColor = MaterialTheme.colorScheme.onPrimary
                                    )
                                ) {
                                    Text(text = "Edit")
                                }
                                Button(
                                    onClick = { onDeleteClick(reservasi.idReservasi) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF2196F3),
                                        contentColor = MaterialTheme.colorScheme.onPrimary
                                    )
                                ) {
                                    Text(text = "Hapus")
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}
