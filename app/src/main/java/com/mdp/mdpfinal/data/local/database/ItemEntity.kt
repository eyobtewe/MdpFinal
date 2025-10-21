package com.mdp.mdpfinal.data.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "inventory_items")
data class ItemEntity(@PrimaryKey(autoGenerate = true)
                      val id: Int = 0,
                      val name: String,
                      val category: String,
                      val price: Double,
                      val quantity: Int
)
    