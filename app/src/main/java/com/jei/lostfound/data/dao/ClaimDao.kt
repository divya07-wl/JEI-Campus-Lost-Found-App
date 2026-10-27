package com.jei.lostfound.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jei.lostfound.data.entity.Claim

@Dao
interface ClaimDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertClaim(claim: Claim): Long

    @Query("SELECT COUNT(*) FROM claims WHERE item_id = :itemId AND claimant_id = :userId AND status = 'PENDING'")
    suspend fun countPendingClaim(itemId: Int, userId: Int): Int
}
