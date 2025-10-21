package com.mdp.mdpfinal.navigation

sealed class Screen(val route: String) {
    object Loading : Screen("loading")
    object Login : Screen("login")
    object Home : Screen("home")
    object Settings : Screen("settings")
    object ItemList : Screen("item_list/{category}") {
        fun createRoute(category: String) = "item_list/$category"
    }
    object ProductDetail : Screen("product_detail/{itemId}") {
        fun createRoute(itemId: Int) = "product_detail/$itemId"
    }
}
    