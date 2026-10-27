package com.jei.lostfound.util

import android.content.Context

/** Demo session. The app runs as the seeded student RAI DIVYA (user_id = 1). */
class SessionManager(context: Context) {
    private val prefs = context.applicationContext
        .getSharedPreferences("lost_found_session", Context.MODE_PRIVATE)

    val currentUserId: Int
        get() = prefs.getInt(KEY_USER_ID, DEFAULT_USER_ID)

    companion object {
        private const val KEY_USER_ID = "user_id"
        const val DEFAULT_USER_ID = 1
    }
}
