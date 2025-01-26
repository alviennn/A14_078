package com.example.myapplication.ui.view.pelanggan

import CustomTopAppBar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.ui.navigation.DestinasiNavigasi
import com.example.myapplication.ui.viewmodel.PenyediaViewModel
import com.example.myapplication.ui.viewmodel.pelanggan.InsertPelangganUiEvent
import com.example.myapplication.ui.viewmodel.pelanggan.InsertPelangganUiState
import com.example.myapplication.ui.viewmodel.pelanggan.InsertPelangganViewModel
import kotlinx.coroutines.launch

object DestinasiInsertPelanggan : DestinasiNavigasi {
    override val route = "insert_pelanggan"
    override val titleRes = "Insert Pelanggan"
    val descriptionRes = "Masukkan data pelanggan"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertPelangganScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InsertPelangganViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CustomTopAppBar(
                modifier = modifier.fillMaxWidth(),
                title = DestinasiInsertPelanggan.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                description = DestinasiInsertPelanggan.descriptionRes,
                navigateUp = navigateBack
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        EntryPelangganBody(
            insertUiState = viewModel.uiPelangganState,
            onPelangganValueChange = viewModel::updateInsertPelangganState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.insertPelanggan()
                    snackbarHostState.showSnackbar("Data pelanggan berhasil disimpan!")
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
fun EntryPelangganBody(
    insertUiState: InsertPelangganUiState,
    onPelangganValueChange: (InsertPelangganUiEvent) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        FormInputPelanggan(
            insertUiEvent = insertUiState.insertPelangganUiEvent,
            onValueChange = onPelangganValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveClick,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2196F3),
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(text = "Simpan")
        }
    }
}

@Composable
fun FormInputPelanggan(
    insertUiEvent: InsertPelangganUiEvent,
    modifier: Modifier = Modifier,
    onValueChange: (InsertPelangganUiEvent) -> Unit = {},
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = insertUiEvent.nama_pelanggan,
            onValueChange = { onValueChange(insertUiEvent.copy(nama_pelanggan = it)) },
            label = { Text(text = "Nama Pelanggan") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = insertUiEvent.no_hp,
            onValueChange = { onValueChange(insertUiEvent.copy(no_hp = it)) },
            label = { Text(text = "No HandPhone") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        if (enabled) {
            Text(
                text = "Semua data harus terisi!",
                modifier = Modifier.padding(12.dp)
            )
        }
        Divider(
            thickness = 8.dp,
            modifier = Modifier.padding(12.dp)
        )
    }
}