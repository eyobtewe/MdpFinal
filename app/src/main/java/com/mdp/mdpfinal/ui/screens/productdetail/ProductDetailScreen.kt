package com.mdp.mdpfinal.ui.screens.productdetail

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mdp.mdpfinal.ui.screens.common.ConfirmationDialog
import com.mdp.mdpfinal.ui.viewmodel.InventoryViewModel
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
        navController: NavController,
        viewModel: InventoryViewModel // = hiltViewModel()
) {
    // Get itemId from navigation arguments
    val itemId = navController.currentBackStackEntry?.arguments?.getInt("itemId")

    // Debug logging
    LaunchedEffect(itemId) { println("ProductDetailScreen: itemId = $itemId") }

    // Load the item directly using the itemId
    val item by
            remember(itemId) {
                        if (itemId != null) {
                            println("ProductDetailScreen: Loading item with id $itemId")
                            viewModel.getItemById(itemId)
                        } else {
                            println("ProductDetailScreen: No itemId provided")
                            flowOf(null)
                        }
                    }
                    .collectAsState(initial = null)

    // Debug logging for item
    LaunchedEffect(item) { println("ProductDetailScreen: item = $item") }

    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) } // <-- ADD THIS STATE

    // Removed automatic navigation back - let user use back button

    // --- SHOW DIALOG IF STATE IS TRUE AND ITEM IS NOT NULL ---
    item?.let { currentItem ->
        if (showEditDialog) {
            EditItemDialog(
                    item = currentItem,
                    onDismiss = { showEditDialog = false },
                    onConfirm = { name, price, quantity ->
                        viewModel.updateItem(
                                itemId = currentItem.id,
                                name = name,
                                category = currentItem.category, // Category does not change
                                price = price.toDoubleOrNull() ?: 0.0,
                                quantity = quantity.toIntOrNull() ?: 0
                        )
                        showEditDialog = false
                    }
            )
        }
        if (showDeleteDialog) {
            ConfirmationDialog(
                    onDismissRequest = { showDeleteDialog = false },
                    onConfirmation = {
                        viewModel.deleteItem(currentItem)
                        showDeleteDialog = false
                        //                    navController.navigateUp() // Go back after deleting
                    },
                    dialogTitle = "Delete Item",
                    dialogText =
                            "Are you sure you want to delete '${currentItem.name}'? This action cannot be undone.",
                    icon = Icons.Default.Delete
            )
        }
    }

    Scaffold(
            topBar = {
                TopAppBar(
                        title = { Text(item?.name ?: "Detail") },
                        navigationIcon = {
                            IconButton(onClick = { navController.navigateUp() }) {
                                Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Back"
                                )
                            }
                        },
                        actions = {
                            if (item != null) {
                                IconButton(onClick = { showEditDialog = true }) {
                                    Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Edit Item"
                                    )
                                }
                                IconButton(onClick = { showDeleteDialog = true }) {
                                    Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete Item"
                                    )
                                }
                            }
                        }
                )
            }
    ) { innerPadding ->
        if (item == null) {
            // Show loading state
            Box(
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                    contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }
        } else {
            val currentItem = item!!
            Column(modifier = Modifier.padding(innerPadding).padding(16.dp).fillMaxSize()) {
                Text(
                        text = "Name: ${currentItem.name}",
                        style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                        text = "Category: ${currentItem.category}",
                        style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                        text = "Price: $${currentItem.price}",
                        style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                        text = "Quantity: ${currentItem.quantity}",
                        style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                // A placeholder for a description if you wanted to add one
                Text(
                        text = "This is a product in ${currentItem.category.lowercase()}.",
                        style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
