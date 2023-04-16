package com.ui.compose

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ui.viewmodel.TotalViewModel

//4つの画面による遷移
//MainActivityのsetContent()でトップ関数として読み込む
@Composable
fun NavigationTop() {
    val navController = rememberNavController()
    val viewModel: TotalViewModel = viewModel()

    NavHost(navController = navController, startDestination = "top") {
        composable("top") {
            Top(onNavigateToExplanation = { navController.navigate("explanation") })
        }
        composable("explanation") {
            Explanation(onNavigateToPlayer = { navController.navigate("player") })
        }
        composable("player") {
            Player(
                onNavigateToResult = { navController.navigate("result") },
                viewModel
            )
        }
        composable("result") {
            Result(
                onNavigateToPlayer = { navController.navigate("player") },
                onNavigateToTop = { navController.popBackStack("top", inclusive = false) },
                viewModel
            )
        }
    }
}