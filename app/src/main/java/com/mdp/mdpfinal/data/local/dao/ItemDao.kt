package com.mdp.mdpfinal.data.local.dao

import androidx.room.*
import com.mdp.mdpfinal.data.local.database.ItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ItemEntity)

    @Update
    suspend fun updateItem(item: ItemEntity)

    @Delete
    suspend fun deleteItem(item: ItemEntity)



    @Query("SELECT * FROM inventory_items WHERE category = :category")
    fun getItemsByCategory(category: String): Flow<List<ItemEntity>>

    @Query("SELECT * FROM inventory_items WHERE id = :itemId")
    fun getItemById(itemId: Int): Flow<ItemEntity?>

}
    