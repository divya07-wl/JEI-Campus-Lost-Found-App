package com.jei.lostfound.data.repository

import com.jei.lostfound.data.AppDatabase
import com.jei.lostfound.data.entity.Claim
import com.jei.lostfound.data.entity.Item
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LostFoundRepository(private val db: AppDatabase) {
    private val itemDao = db.itemDao()
    private val claimDao = db.claimDao()

    fun getAllItems(): Flow<List<Item>> = itemDao.getAllItems()
    fun observeItem(itemId: Int): Flow<Item?> = itemDao.observeItem(itemId)
    fun getMyReports(userId: Int): Flow<List<Item>> = itemDao.getItemsByReporter(userId)

    suspend fun reportItem(item: Item): Long = itemDao.insertItem(item)

    suspend fun submitClaim(itemId: Int, userId: Int, message: String): Boolean {
        if (claimDao.countPendingClaim(itemId, userId) > 0) return false
        val now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        claimDao.insertClaim(
            Claim(itemId = itemId, claimantId = userId, message = message, createdAt = now)
        )
        itemDao.updateStatus(itemId, Item.Status.CLAIMED)
        return true
    }

    suspend fun markReturned(itemId: Int) {
        itemDao.updateStatus(itemId, Item.Status.RETURNED)
    }

    suspend fun deleteItem(itemId: Int) {
        itemDao.deleteItem(itemId)
    }
}
