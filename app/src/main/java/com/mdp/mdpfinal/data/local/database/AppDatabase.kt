package com.mdp.mdpfinal.data.local.database    import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ItemEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemDao(): com.mdp.mdpfinal.data.local.dao.ItemDao
}
    