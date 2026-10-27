package com.jei.lostfound.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jei.lostfound.data.entity.Item
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertItem(item: Item): Long

    @Query("SELECT * FROM items ORDER BY item_date DESC, item_id DESC")
    fun getAllItems(): Flow<List<Item>>

    @Query("SELECT * FROM items WHERE item_id = :itemId")
    fun observeItem(itemId: Int): Flow<Item?>

    @Query("SELECT * FROM items WHERE reporter_id = :userId ORDER BY item_date DESC, item_id DESC")
    fun getItemsByReporter(userId: Int): Flow<List<Item>>

    @Query("UPDATE items SET status = :status WHERE item_id = :itemId")
    suspend fun updateStatus(itemId: Int, status: String): Int

    @Query("DELETE FROM items WHERE item_id = :itemId")
    suspend fun deleteItem(itemId: Int): Int
}
