package com.mdp.mdpfinal.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mdp.mdpfinal.navigation.Screen
import com.mdp.mdpfinal.ui.screens.common.BottomNavigationBar
import com.mdp.mdpfinal.ui.viewmodel.HomeViewModel

@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = hiltViewModel()) {
    // Collect the categories from the ViewModel
    val categories by viewModel.categories.collectAsState()
    val joke by viewModel.joke.collectAsState() // <-- ADD THIS

    Scaffold(bottomBar = { BottomNavigationBar(navController = navController) }) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding).fillMaxSize().padding(16.dp)) {
            // Section for "Today's Joke"
            item {
                Text(text = "Today's Joke", style = MaterialTheme.typography.headlineSmall)
                // Placeholder for the actual joke content
                Text(
                        text = joke, // Display the joke from the ViewModel
                        modifier = Modifier.padding(vertical = 8.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        fontStyle = FontStyle.Italic,
                        lineHeight = 24.sp
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
                Text(text = "Item Categories", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Section for Item Categories
            items(categories) { category ->
                CategoryItem(
                        categoryName = category.name,
                        onCategoryClick = {
                            // Navigate to the ItemList screen with the category name
                            navController.navigate(Screen.ItemList.createRoute(category.name))
                        }
                )
                HorizontalDivider()
            }
        }
    }
}

@Composable
fun CategoryItem(categoryName: String, onCategoryClick: () -> Unit) {
    Text(
            text = categoryName,
            style = MaterialTheme.typography.bodyLarge,
            modifier =
                    Modifier.fillMaxWidth()
                            .clickable(onClick = onCategoryClick) // Make the item clickable
                            .padding(vertical = 16.dp)
    )
}
