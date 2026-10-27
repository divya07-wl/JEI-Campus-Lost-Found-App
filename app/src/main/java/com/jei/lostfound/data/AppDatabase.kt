package com.jei.lostfound.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.jei.lostfound.data.dao.ClaimDao
import com.jei.lostfound.data.dao.ItemDao
import com.jei.lostfound.data.entity.Claim
import com.jei.lostfound.data.entity.Item
import com.jei.lostfound.data.entity.User
import com.jei.lostfound.util.PasswordUtil

@Database(
    entities = [User::class, Item::class, Claim::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
    abstract fun claimDao(): ClaimDao

    companion object {
        const val DATABASE_NAME = "jei_lost_found_database"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    .addCallback(SeedCallback)
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }

    private object SeedCallback : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            val hash = PasswordUtil.sha256("demo1234")
            db.execSQL(
                "INSERT INTO users (student_number, full_name, email, password_hash, created_at) VALUES " +
                    "('2025621033', 'RAI DIVYA', 'divya@jei.ac.kr', '$hash', '2026-10-27 09:00:00'), " +
                    "('20231001', 'Kim Minjun', 'minjun@jei.ac.kr', '$hash', '2026-10-27 09:10:00')"
            )
            db.execSQL(
                "INSERT INTO items (reporter_id, title, category, location, item_date, description, type, status, created_at) VALUES " +
                    "(2, 'Student ID Card', 'ID Card', 'Main Library, 2F', '2026-10-27', 'Black card holder with a JEI student card.', 'LOST', 'OPEN', '2026-10-27 10:00:00'), " +
                    "(1, 'Blue Umbrella', 'Umbrella', 'Student Center', '2026-10-26', 'Found near the cafeteria entrance. It has a small name sticker.', 'FOUND', 'OPEN', '2026-10-26 16:20:00'), " +
                    "(2, 'White Earphones', 'Electronics', 'Engineering Hall', '2026-10-25', 'Wired earphones left in lecture room B-201.', 'FOUND', 'OPEN', '2026-10-25 18:05:00'), " +
                    "(1, 'Notebook', 'Books', 'Humanities Hall', '2026-10-20', 'Green notebook returned to the owner.', 'LOST', 'RETURNED', '2026-10-20 11:30:00')"
            )
        }
    }
}
