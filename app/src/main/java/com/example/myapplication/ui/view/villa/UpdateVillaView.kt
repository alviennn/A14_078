package com.example.myapplication.ui.view

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
import com.example.myapplication.ui.view.EntryVillaBody
import com.example.myapplication.ui.viewmodel.PenyediaViewModel
import com.example.myapplication.ui.viewmodel.villa.UpdateVillaViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object DestinasiUpdateVilla : DestinasiNavigasi {
    override val route = "update_villa"
    override val titleRes = "Update Villa"
    val descriptionRes = "Perbaharui data Villa"
    const val IDVILLA = "idVilla"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateVillaScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    onNavigate: () -> Unit,
    viewModel: UpdateVillaViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold (
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CustomTopAppBar(
                title = DestinasiUpdateVilla.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                description = DestinasiUpdateVilla.descriptionRes,
                navigateUp = onBack
            )
        }
    ){padding ->
        EntryVillaBody(
            modifier = Modifier.padding(padding),
            insertUiState = viewModel.updateVillaUiState,
            onVillaValueChange = viewModel::updateInsertVillaState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.updateVilla()
                    delay(600)
                    withContext(Dispatchers.Main) {
                        onNavigate()
                    }
                }
            }
        )
    }
}