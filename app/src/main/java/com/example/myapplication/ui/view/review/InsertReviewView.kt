@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.myapplication.ui.view.review

import CustomTopAppBar
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.R
import com.example.myapplication.model.Reservasi
import com.example.myapplication.ui.navigation.DestinasiNavigasi
import com.example.myapplication.ui.viewmodel.PenyediaViewModel
import com.example.myapplication.ui.viewmodel.review.InsertReviewUiEvent
import com.example.myapplication.ui.viewmodel.review.InsertReviewUiState
import com.example.myapplication.ui.viewmodel.review.InsertReviewViewModel
import kotlinx.coroutines.launch

object DestinasiInsertReview : DestinasiNavigasi {
    override val route = "insert_review"
    override val titleRes = "Insert Review"
    val descriptionRes = "Ketikkan Pengalaman Anda Bermalam di Villa Kami"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertReviewView(
    navigateBack: () -> Unit,
    viewModel: InsertReviewViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CustomTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = DestinasiInsertReview.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                description = DestinasiInsertReview.descriptionRes,
                navigateUp = navigateBack
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        ) {
            InsertReviewBody(
                insertReviewUiState = viewModel.uiReviewState,
                reservasiList = viewModel.reservasiList,
                onValueChange = viewModel::updateInsertReviewState,
                onSaveClick = {
                    coroutineScope.launch {
                        viewModel.insertReview()
                        snackbarHostState.showSnackbar("Data review berhasil disimpan!")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun InsertReviewBody(
    insertReviewUiState: InsertReviewUiState,
    reservasiList: List<Reservasi>,
    onValueChange: InsertReviewUiEvent.() -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ReviewForm(
            uiEvent = insertReviewUiState.insertReviewUiEvent,
            reservasiList = reservasiList,
            onValueChange = onValueChange
        )
        Button(
            onClick = onSaveClick,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2196F3),
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(text = "Simpan", style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
fun ReviewForm(
    uiEvent: InsertReviewUiEvent,
    reservasiList: List<Reservasi>,
    onValueChange: InsertReviewUiEvent.() -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedReservasiLabel by remember { mutableStateOf("Pilih Reservasi") }

    Column(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Dropdown Reservasi
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                readOnly = true,
                value = selectedReservasiLabel,
                onValueChange = {},
                label = { Text("Reservasi") },
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                reservasiList.forEach { reservasi ->
                    DropdownMenuItem(
                        text = { Text("ID Reservasi: ${reservasi.idReservasi}") },
                        onClick = {
                            selectedReservasiLabel = "ID Reservasi: ${reservasi.idReservasi}"
                            expanded = false
                            uiEvent.copy(id_reservasi = reservasi.idReservasi).onValueChange()
                        }
                    )
                }
            }
        }

        // Rating RadioButtons
        RatingRadioButtons(
            selectedRating = uiEvent.nilai,
            onRatingSelected = { selected ->
                uiEvent.copy(nilai = selected).onValueChange()
            }
        )

        // Input Komentar
        OutlinedTextField(
            value = uiEvent.komentar.orEmpty(),
            onValueChange = { uiEvent.copy(komentar = it).onValueChange() },
            label = { Text("Komentar") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun RatingRadioButtons(
    selectedRating: String,
    onRatingSelected: (String) -> Unit
) {
    val ratings = listOf("Sangat Puas", "Puas", "Biasa", "Tidak Puas", "Sangat Tidak Puas")

    Column {
        ratings.forEach { rating ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                RadioButton(
                    selected = selectedRating == rating,
                    onClick = { onRatingSelected(rating) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = rating, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}