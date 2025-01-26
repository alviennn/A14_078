package com.example.myapplication.ui.view

import CustomTopAppBar
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.R
import com.example.myapplication.model.villa
import com.example.myapplication.ui.viewmodel.PenyediaViewModel
import com.example.myapplication.ui.viewmodel.villa.HomeVillaUiState
import com.example.myapplication.ui.viewmodel.villa.HomeVillaViewModel
import com.example.myapplication.ui.navigation.DestinasiNavigasi

object DestinasiHome : DestinasiNavigasi {
    override val route = "home"
    override val titleRes = "Selamat Datang"
    val descriptionRes = "Mencari Villa Menjadi Lebih Mudah"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(
    modifier: Modifier = Modifier,
    onVillaDetailClick: (Int) -> Unit,
    onVillaClick: () -> Unit,
    onReservasiClick: () -> Unit,
    onPelangganClick: () -> Unit,
    onReviewClick: () -> Unit,
    navigateToItemEntry: () -> Unit,
    viewModel: HomeVillaViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        topBar = {
            CustomTopAppBar(
                modifier = modifier.fillMaxWidth(),
                title = DestinasiHome.titleRes,
                canNavigateBack = false,
                description = DestinasiHome.descriptionRes,
                scrollBehavior = scrollBehavior,
                onRefresh = {}
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MenuBar(
                    onVillaClick = onVillaClick,
                    onReservasiClick = onReservasiClick,
                    onPelangganClick = onPelangganClick,
                    onReviewClick = onReviewClick
                )
                Spacer(modifier = Modifier.height(32.dp))
                HomeStatus(
                    homeUiState = viewModel.villaUiState,
                    retryAction = { viewModel.getVilla() },
                    modifier = Modifier.fillMaxWidth(),
                    onVillaDetailClick = onVillaDetailClick,
                    onDeleteClick = { idVilla -> viewModel.deleteVilla(idVilla) }
                )
            }
        }
    )
}

@Composable
fun MenuBar(
    onVillaClick: () -> Unit,
    onReservasiClick: () -> Unit,
    onPelangganClick: () -> Unit,
    onReviewClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        MenuButton("Villa", R.drawable.house, onVillaClick)
        MenuButton("Reservasi", R.drawable.icon_reservation, onReservasiClick)
        MenuButton("Pelanggan", R.drawable.icon_customer, onPelangganClick)
        MenuButton("Review", R.drawable.review, onReviewClick)
    }
}

@Composable
fun MenuButton(label: String, iconRes: Int, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF2196F3),
                            Color(0xFF21CBF3)
                        )
                    ),
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = label,
                modifier = Modifier.size(32.dp),
                tint = Color.White
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp),
            color = Color(0xFF2196F3)
        )
    }
}

@Composable
fun HomeStatus(
    homeUiState: HomeVillaUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onVillaDetailClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit
) {
    when (homeUiState) {
        is HomeVillaUiState.Loading -> LoadingScreen(modifier = modifier)
        is HomeVillaUiState.Success -> {
            Column {
                Text(
                    text = "Daftar Villa",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    color = Color(0xFF2197F1)
                )
                VillaList(
                    villa = homeUiState.villa,
                    modifier = modifier,
                    onDeleteClick = onDeleteClick,
                    onVillaDetailClick = onVillaDetailClick
                )
            }
        }
        is HomeVillaUiState.Error -> ErrorScreen(retryAction, modifier = modifier)
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                modifier = Modifier.size(150.dp),
                painter = painterResource(id = R.drawable.loading),
                contentDescription = "Memuat"
            )
            Spacer(modifier = Modifier.height(16.dp))
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
                text = "Gagal memuat data",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyMedium
            )
            Button(
                onClick = retryAction,
                modifier = Modifier.padding(top = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2196F3),
                    contentColor = Color.White
                )
            ) {
                Text("Coba Lagi")
            }
        }
    }
}

@Composable
fun VillaList(
    villa: List<villa>,
    modifier: Modifier = Modifier,
    onVillaDetailClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(villa) { villaItem ->
            VillaCard(
                villaItem = villaItem,
                onEditClick = { onVillaDetailClick(villaItem.idVilla) },
                onDeleteClick = onDeleteClick
            )
        }
    }
}

@Composable
fun VillaCard(
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
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE3F2FD)
        )
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.placeholder),
                contentDescription = villaItem.namaVilla,
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = villaItem.namaVilla,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = villaItem.alamat,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
