package com.example.myapplication.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.ui.view.DestinasiDetailVilla
import com.example.myapplication.ui.view.DestinasiHome
import com.example.myapplication.ui.view.DestinasiHomeReservasi
import com.example.myapplication.ui.view.DestinasiHomeReview
import com.example.myapplication.ui.view.DestinasiHomeVilla
import com.example.myapplication.ui.view.DestinasiInsertReservasi
import com.example.myapplication.ui.view.DestinasiInsertVilla
import com.example.myapplication.ui.view.DestinasiUpdateVilla
import com.example.myapplication.ui.view.DetailVillaScreen
import com.example.myapplication.ui.view.HomeReservasiView
import com.example.myapplication.ui.view.HomeReviewView
import com.example.myapplication.ui.view.HomeView
import com.example.myapplication.ui.view.HomeVillaaView
import com.example.myapplication.ui.view.InsertReservasiView
import com.example.myapplication.ui.view.InsertVillaScreen
import com.example.myapplication.ui.view.UpdateVillaScreen
import com.example.myapplication.ui.view.pelanggan.DestinasiDetailPelanggan
import com.example.myapplication.ui.view.pelanggan.DestinasiHomePelanggan
import com.example.myapplication.ui.view.pelanggan.DestinasiInsertPelanggan
import com.example.myapplication.ui.view.pelanggan.DestinasiUpdatePelanggan
import com.example.myapplication.ui.view.pelanggan.DetailPelangganScreen
import com.example.myapplication.ui.view.pelanggan.HomePelangganView
import com.example.myapplication.ui.view.pelanggan.InsertPelangganScreen
import com.example.myapplication.ui.view.pelanggan.UpdatePelangganScreen
import com.example.myapplication.ui.view.reservasi.DestinasiDetailReservasi
import com.example.myapplication.ui.view.reservasi.DestinasiUpdateReservasi
import com.example.myapplication.ui.view.reservasi.DetailReservasiScreen
import com.example.myapplication.ui.view.reservasi.UpdateReservasiScreen
import com.example.myapplication.ui.view.review.DestinasiDetailReview
import com.example.myapplication.ui.view.review.DestinasiInsertReview
import com.example.myapplication.ui.view.review.InsertReviewView
import com.example.myapplication.ui.view.review.UpdateReviewScreen

@Composable
fun PengelolaHalaman(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = DestinasiHome.route,
        modifier = Modifier
    ){
        composable(DestinasiHome.route){
            HomeView(
                onVillaClick = {navController.navigate((DestinasiHomeVilla.route)) },
                navigateToItemEntry = {},
                onReservasiClick = {navController.navigate(DestinasiHomeReservasi.route)},
                onVillaDetailClick = {idVilla ->
                    navController.navigate("${DestinasiDetailVilla.route}/$idVilla")
                    println("PengelolaHalaman: idvilla $idVilla")
                },
                onPelangganClick = {navController.navigate(DestinasiHomePelanggan.route)},
                onReviewClick = {navController.navigate(DestinasiHomeReview.route)}
            )
        }
        composable(DestinasiHomeVilla.route){
            HomeVillaaView(
                navigateToItemEntry = { navController.navigate(DestinasiInsertVilla.route) },
                navigateBack = { navController.navigateUp() },
                onVillaClick = {idVilla ->
                    navController.navigate("${DestinasiDetailVilla.route}/$idVilla")
                    println("PengelolaHalaman: idvilla $idVilla")
                },
            )
        }
        composable(DestinasiHomePelanggan.route){
            HomePelangganView(
                navigateToItemEntry = { navController.navigate(DestinasiInsertPelanggan.route) },
                navigateBack = { navController.navigateUp() },
                navigateToEditPelanggan = { idPelanggan ->
                    navController.navigate("${DestinasiUpdatePelanggan.route}/$idPelanggan")
                },
            )
        }
        composable(DestinasiHomeReview.route){
            HomeReviewView(
                navigateToItemEntry = { navController.navigate(DestinasiInsertReview.route) },
                navigateBack = { navController.navigateUp() },
                onReviewClick = {idReview ->
                    navController.navigate("${DestinasiDetailReview.route}/$idReview")
                    println("PengelolaHalaman: idReview $idReview")
                },
            )
        }
        composable(DestinasiInsertVilla.route){
            InsertVillaScreen(navigateBack = {
                navController.navigate(DestinasiHomeVilla.route){
                    popUpTo(DestinasiHomeVilla.route){inclusive = true}
                }
            })
        }
        composable(DestinasiInsertReview.route){
            InsertReviewView(
                navigateBack = {
                    navController.navigate(DestinasiHomeReview.route) {
                        popUpTo(DestinasiHomeReview.route) { inclusive = true }
                    }
                }
            )
        }
        composable(
            "${DestinasiDetailVilla.route}/{idVilla}",
            arguments = listOf(
                navArgument(DestinasiDetailVilla.IDVILLA){
                    type = NavType.IntType
                }
            )
        ){
            val idVilla = it.arguments?.getInt(DestinasiDetailVilla.IDVILLA)
            idVilla?.let { idVilla ->
                DetailVillaScreen(
                    onBackClick = { navController.navigateUp() },
                    onReservasiClick = { navController.navigate(DestinasiInsertReservasi.route) },
                    onEditClick = {navController.navigate("${DestinasiUpdateVilla.route}/$idVilla")},
                    onDeleteClick = { navController.popBackStack() }
                )
            }
        }
        composable(
            "${DestinasiDetailPelanggan.route}/{idPelanggan}",
            arguments = listOf(
                navArgument(DestinasiDetailPelanggan.IDPELANGGAN){
                    type = NavType.IntType
                }
            )
        ){
            val idPelanggan = it.arguments?.getInt(DestinasiDetailPelanggan.IDPELANGGAN)
            idPelanggan?.let { idPelanggan ->
                DetailPelangganScreen(
                    onBackClick = { navController.navigateUp() },
                    onEditClick = {navController.navigate("${DestinasiUpdatePelanggan.route}/$idPelanggan")},
                    onDeleteClick = { navController.popBackStack() }
                )
            }
        }
        composable(
            "${DestinasiUpdateVilla.route}/{idVilla}",
            arguments = listOf(
                navArgument("idVilla"){type = NavType.IntType}
            )
        ){backStackEntry ->
            val idVilla = backStackEntry.arguments?.getInt(DestinasiUpdateVilla.IDVILLA)
            idVilla?.let { idVilla ->
                UpdateVillaScreen(
                    onBack = { navController.navigateUp() },
                    onNavigate = { navController.navigateUp() }
                )
            }
        }
        composable("${DestinasiUpdateReservasi.route}/{idReservasi}",
            arguments = listOf(
                navArgument("idReservasi") { type = NavType.IntType }
            )
        ) {backStackEntry->
            val idReservasi = backStackEntry.arguments?.getInt(DestinasiUpdateReservasi.IDRESERVASI)
            idReservasi?.let { idReservasi ->
                UpdateReservasiScreen(
                    onBack = { navController.navigateUp() },
                    onNavigate = { navController.navigateUp() }
                )
            }
        }
        composable(DestinasiInsertPelanggan.route){
            InsertPelangganScreen(
                navigateBack = {
                    navController.navigate(DestinasiHomePelanggan.route){
                        popUpTo(DestinasiHomePelanggan.route){inclusive = true}
                    }
                }
            )
        }
        composable(
            "${DestinasiUpdatePelanggan.route}/{idPelanggan}",
            arguments = listOf(
                navArgument("idPelanggan") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val idPelanggan = backStackEntry.arguments?.getInt(DestinasiUpdatePelanggan.IDPELANGGAN)
            idPelanggan?.let {idPelanggan->
                UpdatePelangganScreen(
                    onBack = { navController.navigateUp() },
                    onNavigate = { navController.navigateUp() }
                )
            }
        }
        composable(DestinasiHomeReservasi.route){
            HomeReservasiView(
                navigateToItemEntry = { navController.navigate(DestinasiInsertReservasi.route) },
                navigateBack = { navController.navigateUp() },
                onReservasiClick = { idReservasi ->
                    navController.navigate("${DestinasiDetailReservasi.route}/$idReservasi")
                    println("PengelolaHalaman: idvilla $idReservasi n")
                },
            )
        }
        composable(DestinasiInsertReservasi.route) {
            InsertReservasiView(
                navigateBack = {
                    navController.navigate(DestinasiHomeReservasi.route) {
                        popUpTo(DestinasiHomeReservasi.route) { inclusive = true }
                    }
                }
            )
        }
        composable("${DestinasiDetailReservasi.route}/{idReservasi}",
            arguments = listOf(
                navArgument(DestinasiDetailReservasi.IDRESERVASI){
                    type = NavType.IntType
                }
            )
        ){backStackEntry->
            val idReservasi = backStackEntry.arguments?.getInt(DestinasiDetailReservasi.IDRESERVASI)
            idReservasi?.let {
                DetailReservasiScreen(
                    onBackClick = {navController.navigateUp()},
                    onEditClick = {navController.navigate("${DestinasiUpdateReservasi.route}/$idReservasi")},
                    onDeleteClick = {navController.popBackStack()}
                )
            }
        }
        composable("${DestinasiDetailReview.route}/{idReview}",
            arguments = listOf(
                navArgument(DestinasiDetailReview.IDREVIEW){
                    type = NavType.IntType
                }
            )
        ){
            val idReview = it.arguments?.getInt(DestinasiDetailReview.IDREVIEW)
            idReview?.let { idReview ->
                UpdateReviewScreen(
                    onBack = {navController.navigateUp()},
                    onNavigate = {navController.navigateUp()}
                )
            }
        }
    }
}