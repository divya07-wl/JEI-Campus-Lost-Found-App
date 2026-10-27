package com.jei.lostfound.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "claims",
    foreignKeys = [
        ForeignKey(
            entity = Item::class,
            parentColumns = ["item_id"],
            childColumns = ["item_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["user_id"],
            childColumns = ["claimant_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("item_id"), Index("claimant_id")]
)
data class Claim(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "claim_id") val claimId: Int = 0,
    @ColumnInfo(name = "item_id") val itemId: Int,
    @ColumnInfo(name = "claimant_id") val claimantId: Int,
    @ColumnInfo(name = "message") val message: String,
    @ColumnInfo(name = "status", defaultValue = "PENDING") val status: String = Status.PENDING,
    @ColumnInfo(name = "created_at") val createdAt: String
) {
    object Status {
        const val PENDING = "PENDING"
        const val APPROVED = "APPROVED"
        const val REJECTED = "REJECTED"
    }
}
