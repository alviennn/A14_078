package com.example.myapplication.ui.view.reservasi

import com.example.myapplication.ui.view.EntryReservasiBody
import CustomTopAppBar
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.ui.navigation.DestinasiNavigasi
import com.example.myapplication.ui.viewmodel.PenyediaViewModel
import com.example.myapplication.ui.viewmodel.reservasi.UpdateReservasiViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object DestinasiUpdateReservasi : DestinasiNavigasi {
    override val route = "update_reservasi"
    override val titleRes = "Update Reservasi"
    val descriptionRes = "Perbaharui data Reservasi"
    const val IDRESERVASI = "idReservasi"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateReservasiScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    onNavigate: () -> Unit,
    viewModel: UpdateReservasiViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    LaunchedEffect(Unit) {
        viewModel.loadInitialData()
    }

    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CustomTopAppBar(
                title = DestinasiUpdateReservasi.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                description = DestinasiUpdateReservasi.descriptionRes,
                navigateUp = onBack
            )
        }
    ) { padding ->
        EntryReservasiBody(
            modifier = Modifier.padding(padding),
            villaList = viewModel.villaNames,
            pelangganList = viewModel.pelangganNames,
            insertReservasiUiState = viewModel.updateReservasiUiState,
            onValueChange = viewModel::updateInsertReservasiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.updateReservasi()
                    delay(600)
                    withContext(Dispatchers.Main) {
                        onNavigate()
                    }
                }
            }
        )
    }
}
