package com.jei.lostfound.ui.report

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.jei.lostfound.data.AppDatabase
import com.jei.lostfound.data.entity.Item
import com.jei.lostfound.data.repository.LostFoundRepository
import com.jei.lostfound.util.SessionManager
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ReportItemViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = LostFoundRepository(AppDatabase.getDatabase(application))
    private val userId = SessionManager(application).currentUserId

    fun submit(
        type: String,
        title: String,
        category: String,
        location: String,
        date: String,
        description: String,
        onDone: () -> Unit
    ) {
        viewModelScope.launch {
            val now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            repository.reportItem(
                Item(
                    reporterId = userId,
                    title = title.trim(),
                    category = category,
                    location = location.trim(),
                    itemDate = date,
                    description = description.trim(),
                    type = type,
                    createdAt = now
                )
            )
            onDone()
        }
    }
}
