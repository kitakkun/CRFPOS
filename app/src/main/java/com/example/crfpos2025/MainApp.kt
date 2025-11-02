package com.example.crfpos2025

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.feature_goods.AddGoodsScreen
import com.example.feature_goods.AddGoodsViewModel
import com.example.feature_goods.EditGoodsScreen
import com.example.feature_goods.EditGoodsViewModel
import com.example.feature_goods.GoodsListViewModel
import com.example.feature_goods.GoodsScreen
import com.example.feature_record.EditRecordScreen
import com.example.feature_record.EditRecordViewModel
import com.example.feature_record.RecordScreen
import com.example.feature_sales.SalesScreen
import com.example.feature_sales.SalesViewModel

@Composable
fun MainApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "/") {
        composable("/") {
            MainScreen(
                toSalesScreen = {
                    navController.navigate("/sales/normal")
                },
                toRecordScreen = {
                    navController.navigate("/record")
                },
                toGoodsScreen = {
                    navController.navigate("/goods")
                },
                toGoodsSalesScreen = {
                    navController.navigate("/sales/goods")
                }
            )
        }

        composable("/sales/normal") {
            val viewModel: SalesViewModel = hiltViewModel()
            SalesScreen(
                viewModel = viewModel,
                back = {
                    navController.popBackStack()
                }
            )
        }

        composable("/sales/goods") {
            val viewModel: SalesViewModel = hiltViewModel()
            SalesScreen(
                viewModel = viewModel,
                back = {
                    navController.popBackStack()
                },
                isGoodsOnly = true
            )
        }

        composable("/record") {
            RecordScreen(
                viewModel = hiltViewModel(),
                onClickBack = navController::popBackStack,
                onClickRecordItem = { navController.navigate("/record/${it}") },
            )
        }

        composable(
            "/record/{recordId}",
            arguments = listOf(
                navArgument("recordId") {
                    type = NavType.LongType
                }
            )
        ) {
            val viewModel: EditRecordViewModel = hiltViewModel()
            EditRecordScreen(
                back = {
                    navController.popBackStack()
                },
                viewModel = viewModel,
            )
        }

        composable("/goods") {
            val viewModel: GoodsListViewModel = hiltViewModel()
            GoodsScreen(
                back = {
                    navController.popBackStack()
                },
                toAddGoodsScreen = {
                    navController.navigate("/goods/add")
                },
                viewModel = viewModel,
                toEdit = { id ->
                    navController.navigate("/goods/$id")
                },
            )
        }

        composable("/goods/add") {
            val viewModel: AddGoodsViewModel = hiltViewModel()
            AddGoodsScreen(
                back = {
                    navController.popBackStack()
                },
                viewModel = viewModel,
            )
        }

        composable(
            "/goods/{goodsId}",
            arguments = listOf(
                navArgument("goodsId") {
                    type = NavType.LongType
                }
            )
        ) {
            val viewModel: EditGoodsViewModel = hiltViewModel()
            EditGoodsScreen(
                back = {
                    navController.popBackStack()
                },
                viewModel = viewModel,
            )
        }

    }

}