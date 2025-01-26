package com.example.myapplication.ui.view.pelanggan

import CustomTopAppBar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.example.myapplication.ui.viewmodel.pelanggan.DetailPelangganUiState
import com.example.myapplication.ui.viewmodel.pelanggan.DetailPelangganViewModel

object DestinasiDetailPelanggan : DestinasiNavigasi {
    override val route = "pelanggan_detail"
    override val titleRes = "Detail Pelanggan"
    val descriptionRes = "Deskripsi Pelanggan"
    const val IDPELANGGAN = "idPelanggan"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailPelangganScreen(
    onBackClick: () -> Unit,
    onEditClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailPelangganViewModel = viewModel(factory = PenyediaViewModel.Factory)

) {
    val uiState = viewModel.pelangganDetailState

    Scaffold (
        topBar = {
            CustomTopAppBar(
                title = DestinasiDetailPelanggan.titleRes,
                canNavigateBack = true,
                navigateUp = onBackClick,
                description = DestinasiDetailPelanggan.descriptionRes
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (uiState){
                    is DetailPelangganUiState.Loading -> CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                    is DetailPelangganUiState.Error -> Text(
                        text = "Gagal Memuat Data Pelanggan.",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.align(Alignment.Center)
                    )
                    is DetailPelangganUiState.Success -> {
                        val pelanggan = uiState.pelanggan

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                        ){
                            Card (
                                modifier = Modifier.fillMaxWidth(),
                                elevation = CardDefaults.cardElevation(8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFE3F2FD)
                                )
                            ){
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Text(
                                        text = "Detail Pelanggan",
                                        style = MaterialTheme.typography.titleMedium,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )
                                    Text(
                                        text = "Nama: ${pelanggan.nama_pelanggan}",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Text(
                                        text = "No HP: ${pelanggan.no_hp}",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                            }
                        }

                    }
                }
            }
        }
    )
}