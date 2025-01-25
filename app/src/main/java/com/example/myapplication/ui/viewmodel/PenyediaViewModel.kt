package com.example.myapplication.ui.viewmodel

import InsertReservasiViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myapplication.ReservasiVillaApplications
import com.example.myapplication.ui.viewmodel.pelanggan.DetailPelangganViewModel
import com.example.myapplication.ui.viewmodel.pelanggan.HomePelangganViewModel
import com.example.myapplication.ui.viewmodel.pelanggan.InsertPelangganViewModel
import com.example.myapplication.ui.viewmodel.pelanggan.UpdatePelangganViewModel
import com.example.myapplication.ui.viewmodel.reservasi.DetailReservasiViewModel
import com.example.myapplication.ui.viewmodel.reservasi.HomeReservasiViewModel
import com.example.myapplication.ui.viewmodel.reservasi.UpdateReservasiViewModel
import com.example.myapplication.ui.viewmodel.review.DetailReviewViewModel
import com.example.myapplication.ui.viewmodel.review.HomeReviewViewModel
import com.example.myapplication.ui.viewmodel.review.InsertReviewViewModel
import com.example.myapplication.ui.viewmodel.villa.DetailVillaViewModel
import com.example.myapplication.ui.viewmodel.villa.HomeVillaViewModel
import com.example.myapplication.ui.viewmodel.villa.InsertVillaViewModel
import com.example.myapplication.ui.viewmodel.villa.UpdateVillaViewModel

object PenyediaViewModel {
    val Factory = viewModelFactory {
        initializer {
            HomeVillaViewModel(
                aplikasiRes().container.villaRepository
            )
        }
        initializer {
            InsertVillaViewModel(
                aplikasiRes().container.villaRepository
            )
        }
        initializer {
            DetailVillaViewModel(
                this.createSavedStateHandle(),
                aplikasiRes().container.villaRepository,
            )
        }
        initializer {
            UpdateVillaViewModel(
                this.createSavedStateHandle(),
                aplikasiRes().container.villaRepository
            )
        }
        initializer {
            HomePelangganViewModel(
                aplikasiRes().container.pelangganRepository
            )
        }
        initializer {
            InsertPelangganViewModel(
                aplikasiRes().container.pelangganRepository
            )
        }
        initializer {
            DetailPelangganViewModel(
                this.createSavedStateHandle(),
                aplikasiRes().container.pelangganRepository
            )
        }
        initializer {
            UpdatePelangganViewModel(
                this.createSavedStateHandle(),
                aplikasiRes().container.pelangganRepository
            )
        }
        initializer {
            HomeReservasiViewModel(
                aplikasiRes().container.reservasiRepository,
                aplikasiRes().container.villaRepository
            )
        }
        initializer {
            InsertReservasiViewModel(
                aplikasiRes().container.reservasiRepository,
                aplikasiRes().container.villaRepository,
                aplikasiRes().container.pelangganRepository
            )
        }
        initializer {
            DetailReservasiViewModel(
                this.createSavedStateHandle(),
                aplikasiRes().container.reservasiRepository,
                aplikasiRes().container.villaRepository,
                aplikasiRes().container.pelangganRepository
            )
        }
        initializer {
            UpdateReservasiViewModel(
                this.createSavedStateHandle(),
                aplikasiRes().container.reservasiRepository,
                aplikasiRes().container.villaRepository,
                aplikasiRes().container.pelangganRepository
            )
        }
        initializer {
            HomeReviewViewModel(
                aplikasiRes().container.reviewRepository,
                aplikasiRes().container.reservasiRepository,
                aplikasiRes().container.pelangganRepository,
                aplikasiRes().container.villaRepository
            )
        }
        initializer {
            InsertReviewViewModel(
                aplikasiRes().container.reviewRepository,
                aplikasiRes().container.reservasiRepository
            )
        }
        initializer {
            DetailReviewViewModel(
                this.createSavedStateHandle(),
                aplikasiRes().container.reviewRepository,
                aplikasiRes().container.reservasiRepository
            )
        }
    }
}

fun CreationExtras.aplikasiRes():ReservasiVillaApplications =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ReservasiVillaApplications)