package com.example.myapplication.ui.view.pelanggan

import CustomTopAppBar
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.ui.navigation.DestinasiNavigasi
import com.example.myapplication.ui.viewmodel.PenyediaViewModel
import com.example.myapplication.ui.viewmodel.pelanggan.UpdatePelangganViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object DestinasiUpdatePelanggan : DestinasiNavigasi{
    override val route = "update_pelanggan"
    override val titleRes = "Update Pelanggan"
    val descriptionRes = "Perbaharui data pelanggan"
    const val IDPELANGGAN = "idPelanggan"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdatePelangganScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    onNavigate: () -> Unit,
    viewModel: UpdatePelangganViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold (
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CustomTopAppBar(
                title = DestinasiUpdatePelanggan.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                description = DestinasiUpdatePelanggan.descriptionRes,
                navigateUp = onBack
            )
        }
    ){padding ->
        EntryPelangganBody(
            modifier = Modifier.padding(padding),
            insertUiState = viewModel.uiPelangganState,
            onPelangganValueChange = viewModel::updateInsertPelangganState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.updatePelanggan()
                    delay(600)
                    withContext(Dispatchers.Main) {
                        onNavigate()
                    }
                }
            }
        )
    }
}