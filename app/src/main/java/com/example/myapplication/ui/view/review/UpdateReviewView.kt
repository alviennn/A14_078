package com.example.myapplication.ui.view.review

import CustomTopAppBar
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Updater
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.ui.navigation.DestinasiNavigasi
import com.example.myapplication.ui.viewmodel.PenyediaViewModel
import com.example.myapplication.ui.viewmodel.review.DetailReviewViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object DestinasiDetailReview : DestinasiNavigasi {
    override val route = "review_detail"
    override val titleRes = "Update Review"
    val descriptionRes = "Perbarui data Review"
    const val IDREVIEW = "idReview"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateReviewScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    onNavigate: () -> Unit,
    viewModel: DetailReviewViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    LaunchedEffect (Unit){
        viewModel.loadReservasiList()
    }

    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CustomTopAppBar(
                title = DestinasiDetailReview.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                description = DestinasiDetailReview.descriptionRes,
                navigateUp = onBack
            )
        }
    ) {padding ->
        InsertReviewBody(
            modifier = Modifier.padding(padding),
            reservasiList = viewModel.reservasiList,
            insertReviewUiState = viewModel.detailReviewState,
            onValueChange = viewModel::updateInsertReviewState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.updateReview()
                    delay(600)
                    withContext(Dispatchers.Main) {
                        onNavigate()
                    }
                }
            }
        )
    }
}


