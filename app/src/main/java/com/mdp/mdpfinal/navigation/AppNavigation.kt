package com.mdp.mdpfinal.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mdp.mdpfinal.ui.screens.home.HomeScreen
import com.mdp.mdpfinal.ui.screens.itemlist.ItemListScreen
import com.mdp.mdpfinal.ui.screens.login.LoginScreen
import com.mdp.mdpfinal.ui.screens.productdetail.ProductDetailScreen
import com.mdp.mdpfinal.ui.screens.settings.SettingsScreen
import com.mdp.mdpfinal.ui.viewmodel.AuthState
import com.mdp.mdpfinal.ui.viewmodel.InventoryViewModel
import com.mdp.mdpfinal.ui.viewmodel.LoginViewModel

@Composable
fun AppNavigation(
        modifier: Modifier = Modifier,
        navController: NavHostController = rememberNavController(),
        loginViewModel: LoginViewModel = hiltViewModel()
) {
    val authState by loginViewModel.authState.collectAsState()

    NavHost(
            navController = navController,
            // START AT THE NEW LOADING ROUTE
            startDestination = Screen.Loading.route,
            modifier = modifier
    ) {
        composable(Screen.Loading.route) {
            // You can show a CircularProgressIndicator here if you want
        }
        composable(Screen.Login.route) { LoginScreen(viewModel = loginViewModel) }
        composable(Screen.Home.route) { HomeScreen(navController = navController) }
        composable(Screen.Settings.route) { SettingsScreen(navController = navController) }
        composable(
                route = Screen.ItemList.route,
                arguments = listOf(navArgument("category") { type = NavType.StringType })
        ) { backStackEntry ->
            val inventoryViewModel: InventoryViewModel = hiltViewModel()
            val category = backStackEntry.arguments?.getString("category")
            ItemListScreen(
                    navController = navController,
                    category = category,
                    viewModel = inventoryViewModel
            )
        }
        composable(
                route = Screen.ProductDetail.route,
                arguments = listOf(navArgument("itemId") { type = NavType.IntType })
        ) { backStackEntry ->
            val inventoryViewModel: InventoryViewModel = hiltViewModel()
            ProductDetailScreen(navController = navController, viewModel = inventoryViewModel)
        }
    }

    // Handle initial navigation based on auth state
    LaunchedEffect(authState) {
        when (authState) {
            AuthState.LOGGED_IN -> {
                if (navController.currentDestination?.route == Screen.Loading.route) {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Loading.route) { inclusive = true }
                    }
                }
            }
            AuthState.LOGGED_OUT -> {
                if (navController.currentDestination?.route != Screen.Login.route) {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                }
            }
            AuthState.LOADING -> {
                // Do nothing, we are already on the loading screen
            }
        }
    }
}
