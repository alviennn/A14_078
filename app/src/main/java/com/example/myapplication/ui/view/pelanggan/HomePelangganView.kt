package com.example.myapplication.ui.view.pelanggan

import CustomTopAppBar
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.R
import com.example.myapplication.model.Pelanggan
import com.example.myapplication.ui.navigation.DestinasiNavigasi
import com.example.myapplication.ui.viewmodel.PenyediaViewModel
import com.example.myapplication.ui.viewmodel.pelanggan.HomePelangganUiState
import com.example.myapplication.ui.viewmodel.pelanggan.HomePelangganViewModel

object DestinasiHomePelanggan : DestinasiNavigasi {
    override val route = "home_pelanggan"
    override val titleRes = "Daftar Pelanggan"
    val descriptionRes = "Temukan Pelanggan Anda Disini"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePelangganView(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    navigateToItemEntry: () -> Unit,
    navigateToEditPelanggan: (Int) -> Unit,
    viewModel: HomePelangganViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CustomTopAppBar(
                modifier = modifier.fillMaxWidth(),
                title = DestinasiHomePelanggan.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                description = DestinasiHomePelanggan.descriptionRes,
                onRefresh = { viewModel.getPelanggan() },
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
                Icon(imageVector = Icons.Default.Add, contentDescription = "Tambah Pelanggan")
            }
        }
    ) { innerPadding ->
        HomeStatus(
            homeUiState = viewModel.pelangganUiState,
            retryAction = { viewModel.getPelanggan() },
            modifier = modifier.padding(innerPadding),
            onPelangganClick = navigateToEditPelanggan,
            onDeleteClick = { idPelanggan -> viewModel.deletePelanggan(idPelanggan) }
        )
    }
}


@Composable
fun HomeStatus(
    homeUiState: HomePelangganUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onPelangganClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit
){
    when(homeUiState){
        is HomePelangganUiState.Loading -> LoadingScreen(modifier = modifier)
        is HomePelangganUiState.Success -> PelangganList(pelanggan = homeUiState.pelanggan, modifier = modifier,
            onDeleteClick = onDeleteClick, onPelangganClick = onPelangganClick)
        is HomePelangganUiState.Error -> ErrorScreen(retryAction, modifier = modifier)
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
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
fun ErrorScreen(retryAction: () -> Unit, modifier: Modifier = Modifier) {
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
fun PelangganList(
    pelanggan: List<Pelanggan>,
    modifier: Modifier = Modifier,
    onPelangganClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit,
){
    LazyColumn(
        modifier = modifier.padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ){
        items(pelanggan) { pelangganItem ->
            PelangganCard(
                pelangganItem = pelangganItem,
                onEditClick = { onPelangganClick(pelangganItem.id_pelanggan) },
                onDeleteClick = onDeleteClick
            )
        }
    }
}

@Composable
fun PelangganCard(
    pelangganItem: Pelanggan,
    onDeleteClick: (Int) -> Unit,
    onEditClick: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE3F2FD)
        )
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.customer),
                contentDescription = pelangganItem.nama_pelanggan,
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = pelangganItem.nama_pelanggan,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = pelangganItem.no_hp,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Icon(
                painter = painterResource(id = R.drawable.edit_icon),
                contentDescription = "Edit Pelanggan",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onEditClick(pelangganItem.id_pelanggan) }
            )
            Icon(
                painter = painterResource(id = R.drawable.delete),
                contentDescription = "Hapus Pelanggan",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onDeleteClick(pelangganItem.id_pelanggan) }
            )
        }
    }
}