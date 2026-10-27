package com.jei.lostfound.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "items",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["user_id"],
            childColumns = ["reporter_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["reporter_id"])]
)
data class Item(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "item_id") val itemId: Int = 0,
    @ColumnInfo(name = "reporter_id") val reporterId: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "category") val category: String,
    @ColumnInfo(name = "location") val location: String,
    @ColumnInfo(name = "item_date") val itemDate: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "status", defaultValue = "OPEN") val status: String = Status.OPEN,
    @ColumnInfo(name = "created_at") val createdAt: String
) {
    object Type {
        const val LOST = "LOST"
        const val FOUND = "FOUND"
    }

    object Status {
        const val OPEN = "OPEN"
        const val CLAIMED = "CLAIMED"
        const val RETURNED = "RETURNED"
    }
}
