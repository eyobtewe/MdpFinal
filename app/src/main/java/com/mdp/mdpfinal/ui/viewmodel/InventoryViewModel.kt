package com.mdp.mdpfinal.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdp.mdpfinal.data.local.database.ItemEntity
import com.mdp.mdpfinal.data.repository.InventoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch // <-- Make sure this is imported

@HiltViewModel
class InventoryViewModel
@Inject
constructor(
        private val repository: InventoryRepository,
        private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val category: String = savedStateHandle.get<String>("category") ?: "UNKNOWN"
    private val itemId: StateFlow<Int?> = savedStateHandle.getStateFlow("itemId", null)

    val items: StateFlow<List<ItemEntity>> =
            repository
                    .getItemsByCategory(category)
                    .stateIn(
                            scope = viewModelScope,
                            started = SharingStarted.WhileSubscribed(5000),
                            initialValue = emptyList()
                    )
    @OptIn(ExperimentalCoroutinesApi::class)
    val selectedItem: StateFlow<ItemEntity?> =
            itemId
                    .flatMapLatest { id ->
                        if (id == null) {
                            flowOf(null)
                        } else {
                            repository.getItemById(id)
                        }
                    }
                    .stateIn(
                            scope = viewModelScope,
                            started = SharingStarted.WhileSubscribed(5000),
                            initialValue = null
                    )

    fun addItem(name: String, price: Double, quantity: Int) {
        viewModelScope.launch {
            val newItem =
                    ItemEntity(
                            name = name,
                            category = category, // Use the category of the current screen
                            price = price,
                            quantity = quantity
                    )
            repository.insertItem(newItem)
        }
    }

    fun updateItem(itemId: Int, name: String, category: String, price: Double, quantity: Int) {
        viewModelScope.launch {
            val updatedItem =
                    ItemEntity(
                            id = itemId, // Crucially, we pass the original ID
                            name = name,
                            category = category,
                            price = price,
                            quantity = quantity
                    )
            repository.updateItem(updatedItem)
        }
    }

    fun deleteItem(item: ItemEntity) {
        viewModelScope.launch { repository.deleteItem(item) }
    }

    fun setSelectedItemId(itemId: Int) {
        savedStateHandle["itemId"] = itemId
    }

    fun getItemById(itemId: Int) = repository.getItemById(itemId)
}
