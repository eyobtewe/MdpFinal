package com.mdp.mdpfinal.data.repository

import com.mdp.mdpfinal.data.local.dao.ItemDao
import com.mdp.mdpfinal.data.local.database.ItemEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject // TODO check again if this is the correct import

class InventoryRepository @Inject constructor(
    private val itemDao: ItemDao
) {
    fun getItemsByCategory(category: String): Flow<List<ItemEntity>> {
        return itemDao.getItemsByCategory(category)
    }

    suspend fun insertItem(item: ItemEntity) {
        itemDao.insertItem(item)
    }
    suspend fun updateItem(item: ItemEntity) {
        itemDao.updateItem(item)
    }
    suspend fun deleteItem(item: ItemEntity) {
        itemDao.deleteItem(item)
    }

    fun getItemById(itemId: Int): Flow<ItemEntity?> { // Note: Can be nullable
        return itemDao.getItemById(itemId)
    }

}
    