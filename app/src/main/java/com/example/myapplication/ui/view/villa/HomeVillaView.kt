package com.example.myapplication.ui.view

import CustomTopAppBar
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.R
import com.example.myapplication.model.villa
import com.example.myapplication.ui.navigation.DestinasiNavigasi
import com.example.myapplication.ui.viewmodel.PenyediaViewModel
import com.example.myapplication.ui.viewmodel.reservasi.DetailReservasiViewModel
import com.example.myapplication.ui.viewmodel.villa.HomeVillaUiState
import com.example.myapplication.ui.viewmodel.villa.HomeVillaViewModel
import okhttp3.internal.platform.android.BouncyCastleSocketAdapter.Companion.factory

object DestinasiHomeVilla : DestinasiNavigasi {
    override val route = "home_villa"
    override val titleRes = "Daftar Villa"
    val descriptionRes = "Temukan Villa Anda Disini"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeVillaaView(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    onVillaClick: (Int) -> Unit,
    navigateToItemEntry: () -> Unit,
    viewModel: HomeVillaViewModel = viewModel(factory = PenyediaViewModel.Factory),
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CustomTopAppBar(
                modifier = modifier.fillMaxWidth(),
                title = DestinasiHomeVilla.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                description = DestinasiHomeVilla.descriptionRes,
                onRefresh = { viewModel.getVilla() },
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
                Icon(imageVector = Icons.Default.Add, contentDescription = "Tambah Villa")
            }
        },
    ){innerPadding ->
        HomeeStatus(
            homeUiState = viewModel.villaUiState,
            retryAction = {viewModel.getVilla()},
            modifier = modifier.padding(innerPadding),
            onVillaClick = onVillaClick,
            onDeleteClick = {idVilla -> viewModel.deleteVilla(idVilla)},

        )
    }
}

@Composable
fun HomeeStatus(
    homeUiState: HomeVillaUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onVillaClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit
) {
    when (homeUiState) {
        is HomeVillaUiState.Loading -> LoadingsScreen(modifier = modifier)
        is HomeVillaUiState.Success -> VillasList(villa = homeUiState.villa, modifier = modifier,
            onDeleteClick = onDeleteClick, onVillaClick = onVillaClick)
        is HomeVillaUiState.Error -> ErrorsScreen(retryAction, modifier = modifier)
    }
}

@Composable
fun LoadingsScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                modifier = Modifier.size(150.dp),
                painter = painterResource(id = R.drawable.loading),
                contentDescription = stringResource(id = R.string.loading)
            )
            Text("Memuat data...", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun ErrorsScreen(retryAction: () -> Unit, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.error),
                contentDescription = "Error"
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = R.string.loading_failed),
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyMedium
            )
            Button(onClick = retryAction) {
                Text(stringResource(R.string.retry))
            }
        }
    }
}

@Composable
fun VillasList(
    villa: List<villa>,
    modifier: Modifier = Modifier,
    onVillaClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(villa) { villaItem ->
            VillasCard(
                villaItem = villaItem,
                onEditClick = { onVillaClick(villaItem.idVilla) },
                onDeleteClick = onDeleteClick
            )
        }
    }
}


@Composable
fun VillasCard(
    villaItem: villa,
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
                painter = painterResource(id = R.drawable.placeholder),
                contentDescription = villaItem.namaVilla,
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = villaItem.namaVilla,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Kamar tersedia: ${villaItem.kamarTersedia}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Icon(
                painter = painterResource(id = R.drawable.delete),
                contentDescription = "Hapus Villa",
                modifier = Modifier
                    .size(24.dp)
                    .padding(start = 8.dp)
                    .clickable { onDeleteClick(villaItem.idVilla) }
            )
        }
    }
}
