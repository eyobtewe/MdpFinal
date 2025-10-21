package com.mdp.mdpfinal.ui.screens.itemlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.isEmpty
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mdp.mdpfinal.data.local.database.ItemEntity
import com.mdp.mdpfinal.navigation.Screen
import com.mdp.mdpfinal.ui.screens.common.ConfirmationDialog
import com.mdp.mdpfinal.ui.screens.productdetail.EditItemDialog
import com.mdp.mdpfinal.ui.viewmodel.InventoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemListScreen(
        navController: NavController,
        // The category is passed for the TopAppBar title
        category: String?,
        viewModel: InventoryViewModel // = hiltViewModel()
) {
    val items by viewModel.items.collectAsState()

    // --- ADD THIS STATE ---
    // State to control the visibility of the AddItemDialog
    var showDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf<ItemEntity?>(null) }

    // --- SHOW DIALOG IF STATE IS TRUE ---
    if (showDialog) {
        AddItemDialog(
                onDismiss = { showDialog = false },
                onConfirm = { name, category, price, quantity ->
                    // Convert to correct types and call the ViewModel
                    viewModel.addItem(
                            name = name,
                            price = price.toDoubleOrNull() ?: 0.0,
                            quantity = quantity.toIntOrNull() ?: 0
                    )
                    showDialog = false // Close the dialog after confirming
                }
        )
    }

    // Edit Dialog
    if (showEditDialog && selectedItem != null) {
        EditItemDialog(
                item = selectedItem!!,
                onDismiss = { showEditDialog = false },
                onConfirm = { name, price, quantity ->
                    viewModel.updateItem(
                            itemId = selectedItem!!.id,
                            name = name,
                            category = selectedItem!!.category,
                            price = price.toDoubleOrNull() ?: 0.0,
                            quantity = quantity.toIntOrNull() ?: 0
                    )
                    showEditDialog = false
                }
        )
    }

    // Delete Dialog
    if (showDeleteDialog && selectedItem != null) {
        ConfirmationDialog(
                onDismissRequest = { showDeleteDialog = false },
                onConfirmation = {
                    viewModel.deleteItem(selectedItem!!)
                    showDeleteDialog = false
                },
                dialogTitle = "Delete Item",
                dialogText =
                        "Are you sure you want to delete '${selectedItem!!.name}'? This action cannot be undone.",
                icon = Icons.Default.Delete
        )
    }

    Scaffold(
            topBar = {
                TopAppBar(
                        title = { Text(text = category ?: "Items") },
                        navigationIcon = {
                            IconButton(onClick = { navController.navigateUp() }) {
                                Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Back"
                                )
                            }
                        }
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { showDialog = true }) {
                    Icon(Icons.Default.Add, contentDescription = "Add Item")
                }
            }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            if (items.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No items in this category. Add one!")
                }
            } else {
                LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(items) { item ->
                        ItemCard(
                                item = item,
                                onClick = {
                                    navController.navigate(
                                            Screen.ProductDetail.createRoute(item.id)
                                    )
                                },
                                onEdit = {
                                    selectedItem = item
                                    showEditDialog = true
                                },
                                onDelete = {
                                    selectedItem = item
                                    showDeleteDialog = true
                                }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ItemCard(item: ItemEntity, onClick: () -> Unit, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f).clickable { onClick() }) {
                Text(text = item.name, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                        text = "$${item.price} x ${item.quantity} = $${item.price * item.quantity}",
                        style = MaterialTheme.typography.bodyMedium
                )
            }
            Row {
                IconButton(onClick = onEdit) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Item")
                }
                IconButton(onClick = onDelete) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Item")
                }
            }
        }
    }
}
