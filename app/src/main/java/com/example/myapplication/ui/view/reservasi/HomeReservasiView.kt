package com.example.myapplication.ui.view

import CustomTopAppBar
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.R
import com.example.myapplication.model.Reservasi
import com.example.myapplication.ui.navigation.DestinasiNavigasi
import com.example.myapplication.ui.viewmodel.reservasi.HomeReservasiUiState
import com.example.myapplication.ui.viewmodel.reservasi.HomeReservasiViewModel
import com.example.myapplication.ui.viewmodel.PenyediaViewModel

object DestinasiHomeReservasi : DestinasiNavigasi {
    override val route = "home_reservasi"
    override val titleRes = "Daftar Reservasi"
    val descriptionRes = "Lihat dan Kelola Reservasi Anda"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeReservasiView(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    onReservasiClick: (Int) -> Unit,
    navigateToItemEntry: () -> Unit,
    viewModel: HomeReservasiViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CustomTopAppBar(
                modifier = modifier.fillMaxWidth(),
                title = DestinasiHomeReservasi.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                description = DestinasiHomeReservasi.descriptionRes,
                onRefresh = { viewModel.getReservasi() },
                navigateUp = navigateBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToItemEntry,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(16.dp),
                containerColor = Color(0xFF2196F3),
                contentColor = Color.White
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Tambah Reservasi")
            }
        },
    ) { innerPadding ->
        HomeReservasiStatus(
            homeUiState = viewModel.reservasiUiState,
            retryAction = { viewModel.getReservasi() },
            modifier = modifier.padding(innerPadding),
            onReservasiClick = onReservasiClick,
            onDeleteClick = { idReservasi -> viewModel.deleteReservasi(idReservasi) }
        )
    }
}

@Composable
fun HomeReservasiStatus(
    homeUiState: HomeReservasiUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onReservasiClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit
) {
    when (homeUiState) {
        is HomeReservasiUiState.Loading -> LoadingScreen(modifier = modifier)
        is HomeReservasiUiState.Success -> ReservasiList(
            reservasi = homeUiState.reservasi,
            villa = homeUiState.villas,
            modifier = modifier,
            onReservasiClick = onReservasiClick,
            onDeleteClick = onDeleteClick
        )
        is HomeReservasiUiState.Error -> ErrorScreen(retryAction, modifier = modifier)
    }
}

@Composable
fun ReservasiList(
    reservasi: List<Reservasi>,
    villa: List<com.example.myapplication.model.villa>,
    modifier: Modifier = Modifier,
    onReservasiClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(reservasi) { reservasiItem ->
            val villaName = villa.find { it.idVilla == reservasiItem.idVilla }?.namaVilla.orEmpty()
            ReservasiCard(
                reservasiItem = reservasiItem,
                villaName = villaName,
                onEditClick = { onReservasiClick(reservasiItem.idReservasi) },
                onDeleteClick = onDeleteClick
            )
        }
    }
}

@Composable
fun ReservasiCard(
    reservasiItem: Reservasi,
    villaName: String,
    onDeleteClick: (Int) -> Unit,
    onEditClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onEditClick)
            .padding(bottom = 12.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE3F2FD)
        )
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.reservasi),
                contentDescription = villaName,
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "ID: ${reservasiItem.idReservasi}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = villaName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Tanggal: ${reservasiItem.checkIn}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Tanggal: ${reservasiItem.checkOut}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Jumlah Kamar: ${reservasiItem.jumlahKamar}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Icon(
                painter = painterResource(id = R.drawable.delete),
                contentDescription = "Hapus Reservasi",
                modifier = Modifier
                    .size(24.dp)
                    .padding(start = 8.dp)
                    .clickable { onDeleteClick(reservasiItem.idReservasi) }
            )
        }
    }
}