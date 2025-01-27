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
import com.example.myapplication.model.Pelanggan
import com.example.myapplication.model.Reservasi
import com.example.myapplication.model.Review
import com.example.myapplication.model.villa
import com.example.myapplication.ui.navigation.DestinasiNavigasi
import com.example.myapplication.ui.viewmodel.PenyediaViewModel
import com.example.myapplication.ui.viewmodel.review.HomeReviewUiState
import com.example.myapplication.ui.viewmodel.review.HomeReviewViewModel

object DestinasiHomeReview : DestinasiNavigasi {
    override val route = "home_review"
    override val titleRes = "Daftar Review"
    val descriptionRes = "Lihat Review dari Pelanggan Kami"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeReviewView(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    onReviewClick: (Int) -> Unit,
    navigateToItemEntry: () -> Unit,
    viewModel: HomeReviewViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CustomTopAppBar(
                modifier = modifier.fillMaxWidth(),
                title = DestinasiHomeReview.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                description = DestinasiHomeReview.descriptionRes,
                onRefresh = { viewModel.getReview() },
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
                Icon(imageVector = Icons.Default.Add, contentDescription = "Tambah Review")
            }
        }
    ) { innerPadding ->
        HomeReviewStatus(
            homeUiState = viewModel.reviewUiState,
            retryAction = { viewModel.getReview() },
            modifier = modifier.padding(innerPadding),
            onReviewClick = onReviewClick
        )
    }
}

@Composable
fun HomeReviewStatus(
    homeUiState: HomeReviewUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onReviewClick: (Int) -> Unit
) {
    when (homeUiState) {
        is HomeReviewUiState.Loading -> LoadingScreen(modifier = modifier)
        is HomeReviewUiState.Success -> ReviewList(
            reviews = homeUiState.review,
            pelanggan = homeUiState.pelanggan,
            reservasi = homeUiState.reserverasi,
            villa = homeUiState.villa,
            modifier = modifier,
            onReviewClick = onReviewClick
        )
        is HomeReviewUiState.Error -> ErrorScreen(retryAction = retryAction, modifier = modifier)
    }
}

@Composable
fun ReviewList(
    reviews: List<Review>,
    pelanggan: List<Pelanggan>,
    reservasi: List<Reservasi>,
    villa: List<villa>,
    modifier: Modifier = Modifier,
    onReviewClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(reviews) { review ->
            val reservasiItem = reservasi.find { it.idReservasi == review.id_reservasi }
            val pelangganItem = pelanggan.find { it.id_pelanggan == reservasiItem?.id_pelanggan }
            val villaItem = villa.find { it.idVilla == reservasiItem?.idVilla }
            if (pelangganItem != null && villaItem != null) {
                ReviewCard(
                    review = review,
                    pelanggan = pelangganItem,
                    villaName = villaItem.namaVilla,
                    onReviewClick = onReviewClick
                )
            }
        }
    }
}


@Composable
fun ReviewCard(
    review: Review,
    pelanggan: Pelanggan,
    villaName: String,
    onReviewClick: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { onReviewClick(review.idReview) })
            .padding(bottom = 12.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE3F2FD)
        )
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.review),
                contentDescription = "Review ${review.idReview}",
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${pelanggan.nama_pelanggan}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Villa: $villaName",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Nilai: ${review.nilai}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Komentar: ${review.komentar}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}